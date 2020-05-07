package com.example.proficiencyexercise.base

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class AppDelegate : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
    }
}