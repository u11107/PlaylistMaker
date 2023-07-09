package com.example.playlistmaker.setting.domain.interactor

import com.example.playlistmaker.setting.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}