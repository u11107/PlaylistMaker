package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}