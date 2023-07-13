package com.example.playlistmaker.setting.ui.view_model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.setting.domain.interactor.SettingsInteractor
import com.example.playlistmaker.setting.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.util.App

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
    private val application: App,
) : AndroidViewModel(application) {

    private val themeSettings = MutableLiveData<ThemeSettings>()
    val themeSettingsState: LiveData<ThemeSettings> = themeSettings

    init {
        themeSettings.postValue(settingsInteractor.getThemeSettings())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val settings = ThemeSettings(darkThemeEnabled)
        themeSettings.postValue(settings)
        settingsInteractor.updateThemeSetting(settings)
        application.switchTheme(darkThemeEnabled)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun supportEmail() {
        sharingInteractor.openSupport()
    }

    fun openAgreement() {
        sharingInteractor.openTerms()
    }
}