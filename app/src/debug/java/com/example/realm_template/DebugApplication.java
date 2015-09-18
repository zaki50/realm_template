package com.example.realm_template;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

public class DebugApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!isRunningOnJvm()) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                                    .withMetaTables()
                                    .withDescendingOrder()
                                    .withLimit(1000)
                                    .build())
                            .build());
        }
    }

    private boolean isRunningOnJvm() {
        final String vmName = System.getProperty("java.vm.name");
        return vmName != null && vmName.startsWith("Java");
    }
}
