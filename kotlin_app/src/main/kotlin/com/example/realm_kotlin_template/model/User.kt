package com.example.realm_kotlin_template.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {

    @PrimaryKey
    var name: String? = null
    var age: Int = 0
}
