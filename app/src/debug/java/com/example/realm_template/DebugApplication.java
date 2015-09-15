package com.example.realm_template;

import android.content.pm.PackageManager;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

public class DebugApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!isOnRobolectricTest()) {
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

    private boolean isOnRobolectricTest() {
        return getPackageManager().getClass().getName().startsWith("org.robolectric.");
    }
}
