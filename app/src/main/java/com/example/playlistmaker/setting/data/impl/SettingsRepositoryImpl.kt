package com.example.playlistmaker.setting.data.impl

import com.example.playlistmaker.setting.domain.repository.SettingsRepository
import com.example.playlistmaker.setting.data.storage.SettingsThemeStorage
import com.example.playlistmaker.setting.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val storage: SettingsThemeStorage): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return storage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.saveThemeSettings(settings)
    }

}