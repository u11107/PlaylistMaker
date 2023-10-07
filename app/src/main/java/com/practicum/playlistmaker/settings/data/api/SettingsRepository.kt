package com.practicum.playlistmaker.settings.data.api

import com.practicum.playlistmaker.settings.domain.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
    fun applyAppTheme()
}