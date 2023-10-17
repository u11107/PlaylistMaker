package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings = settingsRepository.getThemeSettings()

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}