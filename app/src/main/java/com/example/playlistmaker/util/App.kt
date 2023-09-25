package com.example.playlistmaker.util

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dbModule
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingsModule
import com.example.playlistmaker.di.sharingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import java.text.SimpleDateFormat
import java.util.Locale

class App : Application() {

    companion object {
        const val PLAYLIST_MAKER_SHARED_PREFS = "playlist_maker_shared_prefs"
        lateinit var sharedMemory: SharedPreferences
        fun Long.formatTime() : String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
        }
    }

    var darkTheme = false
    override fun onCreate() {
        super.onCreate()

        org.koin.core.context.startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    playerModule,
                    searchModule,
                    settingsModule,
                    sharingModule,
                    mediaModule,
                    dbModule
                )
            )
        }

        sharedMemory = getSharedPreferences(PLAYLIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
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