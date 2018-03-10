package com.kissybnts.udemykotlinandroid5.controller

import android.app.Application
import com.kissybnts.udemykotlinandroid5.utils.SharedPreferences

class App : Application() {
    companion object {
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        prefs = SharedPreferences(applicationContext)
        super.onCreate()
    }
}