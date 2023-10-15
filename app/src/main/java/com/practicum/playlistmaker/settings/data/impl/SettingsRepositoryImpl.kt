package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.ThemeSettings

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    override fun getThemeSettings() =
        ThemeSettings(sharedPreferences.getBoolean(DARK_THEME_ENABLED, false))

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_ENABLED, settings.darkThemeEnabled)
            .apply()
        applyAppTheme()
    }

    override fun applyAppTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (getThemeSettings().darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        private const val DARK_THEME_ENABLED = "DARK_THEME_ENABLED"
    }
}