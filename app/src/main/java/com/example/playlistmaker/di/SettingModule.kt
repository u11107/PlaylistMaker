package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.setting.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.setting.data.storage.SettingsThemeStorage
import com.example.playlistmaker.setting.data.storage.SharedPrefsThemeStorage
import com.example.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.setting.domain.interactor.SettingsInteractor
import com.example.playlistmaker.setting.domain.repository.SettingsRepository
import com.example.playlistmaker.setting.ui.view_model.SettingsViewModel
import com.example.playlistmaker.util.App
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    viewModel {
        SettingsViewModel(
            settingsInteractor = get(),
            sharingInteractor = get(),
            androidApplication() as App
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(storage = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    single<SettingsThemeStorage> {
        SharedPrefsThemeStorage(sharedPreferences = get())
    }

    single {
        androidContext()
            .getSharedPreferences(
                "local_storage", Context.MODE_PRIVATE
            )
    }

}