package com.example.playlistmaker.setting.domain.impl

import com.example.playlistmaker.setting.domain.repository.SettingsRepository
import com.example.playlistmaker.setting.domain.interactor.SettingsInteractor
import com.example.playlistmaker.setting.domain.model.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}