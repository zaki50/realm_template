package com.example.realm_kotlin_template

import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun provideRealmConfiguration(): RealmConfiguration {
        val builder = RealmConfiguration.Builder()
                .schemaVersion(Migration.SCHEMA_VERSION)
                .migration(Migration())
        if (BuildConfig.DEBUG) {
            builder.deleteRealmIfMigrationNeeded()
        }
        return builder.build()
    }

    @Provides
    internal fun provideDefaultRealm(config: RealmConfiguration): Realm {
        return Realm.getInstance(config)
    }
}
