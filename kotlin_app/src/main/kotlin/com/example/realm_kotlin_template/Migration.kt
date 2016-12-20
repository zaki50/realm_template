package com.example.realm_kotlin_template

import io.realm.DynamicRealm
import io.realm.Realm
import io.realm.RealmMigration

class Migration : RealmMigration {

    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        //if (oldVersion == 0) {
        //    // migrate to 1
        //
        //    // write migration code here
        //
        //    oldVersion++;
        //}

        if (oldVersion != SCHEMA_VERSION) {
            throw RuntimeException("unexpected scheme version. expected: $SCHEMA_VERSION, actual: $oldVersion")
        }
    }

    companion object {
        // increment this if schema changed
        val SCHEMA_VERSION: Long = 0
    }
}
