package com.example.realm_kotlin_template

import android.app.Application
import android.support.annotation.VisibleForTesting

import com.example.realm_kotlin_template.prngfix.PRNGFixes
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

import io.realm.Realm

open class MyApplication : Application() {

    @VisibleForTesting
    var component: ApplicationComponent? = null
    var refWatcher: RefWatcher? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        PRNGFixes.apply()

        Realm.init(this)

        component = DaggerApplicationComponent.create()
        refWatcher = LeakCanary.install(this)
    }

    companion object {
        var instance: MyApplication? = null
            private set
    }

}
