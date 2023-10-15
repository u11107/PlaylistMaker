package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorites.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.favorites.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.playlist_details.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist_details.domain.impl.PlaylistInteractorImpl
import com.practicum.playlistmaker.playlist_creation.domain.api.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesInteractor
import com.practicum.playlistmaker.playlist_creation.domain.impl.PlaylistsDbInteractorImpl
import com.practicum.playlistmaker.playlist_creation.domain.impl.PlaylistsFilesInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    singleOf(::PlayerInteractorImpl) bind PlayerInteractor::class
    singleOf(::SearchInteractorImpl) bind SearchInteractor::class
    singleOf(::SharingInteractorImpl) bind SharingInteractor::class
    singleOf(::SettingsInteractorImpl) bind SettingsInteractor::class
    singleOf(::FavoritesInteractorImpl) bind FavoritesInteractor::class
    singleOf(::PlaylistsDbInteractorImpl) bind PlaylistsDbInteractor::class
    singleOf(::PlaylistsFilesInteractorImpl) bind PlaylistsFilesInteractor::class
    singleOf(::PlaylistInteractorImpl) bind PlaylistInteractor::class
}