package com.example.realm_template;

import android.app.Application;

import com.example.realm_template.prngfix.PRNGFixes;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    private static MyApplication self;

    public static MyApplication getInstance() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;

        PRNGFixes.apply();
        setupRealm();
    }

    private void setupRealm() {
        final RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(Migration.SCHEMA_VERSION)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
