package com.david.haru.bingmaptutorial

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by David Harush
 * On 09/03/2021.
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    companion object {
        lateinit var instance: App
            private set
        val applicationContext: Context
            get() = instance.applicationContext
    }

}