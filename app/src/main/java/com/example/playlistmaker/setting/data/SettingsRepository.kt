package com.example.playlistmaker.setting.data

import com.example.playlistmaker.setting.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}