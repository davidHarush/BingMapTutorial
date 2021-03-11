package com.david.haru.bingmaptutorial.util


import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.david.haru.bingmaptutorial.App

/**
 * Created by David Harush
 * On 09/03/2021.
 */


fun getAppContext() = App.applicationContext

fun showToast(msg: String = "", duration: Int = Toast.LENGTH_SHORT) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(getAppContext(), msg, duration).show()
    }
}

fun isDarkThemeOn(): Boolean {
    return getAppContext().resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}


