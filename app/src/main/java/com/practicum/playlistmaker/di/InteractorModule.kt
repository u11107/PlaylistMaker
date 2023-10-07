package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorites.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.favorites.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.playlists_creation.domain.api.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.playlists_creation.domain.api.local_files.PlaylistsFilesInteractor
import com.practicum.playlistmaker.playlists_creation.domain.impl.PlaylistsDbInteractorImpl
import com.practicum.playlistmaker.playlists_creation.domain.impl.PlaylistsFilesInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<PlaylistsDbInteractor> {
        PlaylistsDbInteractorImpl(get())
    }

    single<PlaylistsFilesInteractor> {
        PlaylistsFilesInteractorImpl(get())
    }
}