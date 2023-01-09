package com.sunnyweather.android

import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object{
        @Suppress("StaticFieldLeak")
        lateinit var context : Context
        const val TOKEN = "Yours token"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
