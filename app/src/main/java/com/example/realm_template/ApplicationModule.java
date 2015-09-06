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
    RealmConfiguration provideRealmConfiguration(Context applicationContext) {
        return new RealmConfiguration.Builder(applicationContext)
                .schemaVersion(Migration.SCHEMA_VERSION)
                .migration(new Migration())
                .build();
    }

    @Provides
    Realm provideDefaultRealm(RealmConfiguration config) {
        return Realm.getInstance(config);
    }
}