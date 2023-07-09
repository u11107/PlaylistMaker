package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.impl.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.storage.SharedPrefsHistoryStorage
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.setting.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.setting.data.storage.SharedPrefsThemeStorage
import com.example.playlistmaker.setting.domain.interactor.SettingsInteractor
import com.example.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

object Creator {

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val sharedPrefs =
            context.getSharedPreferences(SharedPrefsThemeStorage.DARK_THEME, Context.MODE_PRIVATE)
        val repository = SettingsRepositoryImpl(SharedPrefsThemeStorage(sharedPrefs))
        return SettingsInteractorImpl(repository)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        val externalNavigator = ExternalNavigatorImpl(context)
        return SharingInteractorImpl(externalNavigator)
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        val sharedPrefs = context.getSharedPreferences(SharedPrefsHistoryStorage.TRACK_SEARCH_HISTORY, Context.MODE_PRIVATE)
        return SearchInteractorImpl(
            SearchRepositoryImpl(RetrofitNetworkClient(), SharedPrefsHistoryStorage(sharedPrefs))
        )
    }

    fun providePlayerInteractor () = PlayerInteractorImpl(PlayerRepositoryImpl())
}