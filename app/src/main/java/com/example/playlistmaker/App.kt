package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.util.Locale

class App : Application() {

    companion object {
        const val EXAMPLE_PREFERENCES = "example_preferences"
        const val KEY_THEME = "key"
        lateinit var sharedMemory: SharedPreferences
        const val TRACK = "track"
        fun formatTime(millis : Long) : String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
        }
    }

    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        sharedMemory = getSharedPreferences(EXAMPLE_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedMemory.getBoolean(KEY_THEME, false)
        switchTheme(darkTheme)
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedMemory.edit()
            .putBoolean(KEY_THEME, darkTheme)
            .apply()
    }
}