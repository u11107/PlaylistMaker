package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.data.impl.FavouritesRepositoryImpl
import com.practicum.playlistmaker.media.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker.media.data.mapper.PlaylistDbMapper
import com.practicum.playlistmaker.media.data.mapper.PlaylistTrackDbMapper
import com.practicum.playlistmaker.media.data.mapper.TrackDbMapper
import com.practicum.playlistmaker.media.domain.api.FavouritesRepository
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    factory {
        TrackDbMapper()
    }

    factory {
        PlaylistDbMapper(get())
    }

    factory {
        PlaylistTrackDbMapper()
    }
}