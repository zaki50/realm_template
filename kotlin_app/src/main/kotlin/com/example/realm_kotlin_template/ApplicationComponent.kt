package com.example.realm_kotlin_template

import javax.inject.Singleton

import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class))
@Singleton
interface ApplicationComponent {
    fun inject(target: MainActivity)
}
