package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorites.data.FavoritesRepositoryImpl
import com.practicum.playlistmaker.favorites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorites.domain.api.FavoritesRepository
import com.practicum.playlistmaker.playlist_details.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist_details.domain.api.PlaylistRepository
import com.practicum.playlistmaker.playlist_creation.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.playlist_creation.data.db.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.playlist_creation.data.local_files.PlaylistsFilesRepositoryImpl
import com.practicum.playlistmaker.playlist_creation.domain.api.db.PlaylistsRepository
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesRepository
import com.practicum.playlistmaker.search.data.api.SearchRepository
import com.practicum.playlistmaker.search.data.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
    singleOf(::TrackDbConverter)
    singleOf(::FavoritesRepositoryImpl) bind FavoritesRepository::class
    singleOf(::PlaylistDbConverter)
    singleOf(::PlaylistsRepositoryImpl) bind PlaylistsRepository::class
    singleOf(::PlaylistsFilesRepositoryImpl) bind PlaylistsFilesRepository::class
    singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class
}