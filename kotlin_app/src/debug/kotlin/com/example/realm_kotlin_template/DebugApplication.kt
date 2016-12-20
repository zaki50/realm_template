package com.example.realm_kotlin_template

import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider

class DebugApplication : MyApplication() {
    public override fun onCreate() {
        super.onCreate()

        if (!isRunningOnJvm) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                                    .withMetaTables()
                                    .withDescendingOrder()
                                    .withLimit(1000)
                                    .build())
                            .build())
        }
    }

    private val isRunningOnJvm: Boolean
        get() {
            val vmName = System.getProperty("java.vm.name") ?: return false
            return (vmName.startsWith("Java") || vmName.startsWith("OpenJDK"))
        }
}
