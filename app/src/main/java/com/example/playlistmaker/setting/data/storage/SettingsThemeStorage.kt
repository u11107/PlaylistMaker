package com.example.playlistmaker.setting.data.storage

import com.example.playlistmaker.setting.domain.model.ThemeSettings

interface SettingsThemeStorage {
    fun saveThemeSettings(settings: ThemeSettings)
    fun getThemeSettings(): ThemeSettings
}