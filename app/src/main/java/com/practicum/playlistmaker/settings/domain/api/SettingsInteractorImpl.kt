package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractor

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings() = settingsRepository.getThemeSettings()

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}