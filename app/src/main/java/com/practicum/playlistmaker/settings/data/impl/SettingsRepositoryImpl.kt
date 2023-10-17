package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(isDarkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, settings.isDarkTheme)
            .apply()
    }

    companion object {
        const val DARK_THEME_KEY = "dark_theme"
    }
}