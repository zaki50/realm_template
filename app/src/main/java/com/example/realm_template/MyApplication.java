package com.example.realm_template;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.example.realm_template.prngfix.PRNGFixes;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApplication extends Application {
    private static MyApplication self;
    public static MyApplication getInstance() {
        return self;
    }

    @VisibleForTesting
    public ApplicationComponent component;
    private RefWatcher refWatcher;

    public ApplicationComponent getComponent() {
        return component;
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;

        PRNGFixes.apply();

        component = DaggerApplicationComponent.create();
        refWatcher = LeakCanary.install(this);
    }

}
