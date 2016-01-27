package com.example.realm_template;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmMigration;

public class Migration implements RealmMigration {
    // increment this if schema changed
    public static final long SCHEMA_VERSION = 0;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        //if (oldVersion == 0) {
        //    // migrate to 1
        //
        //    // write migration code here
        //
        //    oldVersion++;
        //}

        if (oldVersion != SCHEMA_VERSION) {
            throw new RuntimeException("unexpected scheme version. expected: " + SCHEMA_VERSION + ", actual: " + oldVersion);
        }
    }
}
