package com.example.realm_template;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class ApplicationModule {

    @Provides
    Context provideApplicationContext() {
        return MyApplication.getInstance();
    }

    @Provides
    @Singleton
    RealmConfiguration provideRealmConfiguration() {
        final RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .schemaVersion(Migration.SCHEMA_VERSION)
                .migration(new Migration());
        if (BuildConfig.DEBUG) {
            builder.deleteRealmIfMigrationNeeded();
        }
        return builder.build();
    }

    @Provides
    Realm provideDefaultRealm(RealmConfiguration config) {
        return Realm.getInstance(config);
    }
}