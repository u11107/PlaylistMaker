package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.convertor.DbConverter
import com.example.playlistmaker.media.data.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.media.data.impl.LocalStorageImpl
import com.example.playlistmaker.media.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.media.data.local_storage.LocalStorage
import com.example.playlistmaker.media.domain.api.FavouriteTracksInteractor
import com.example.playlistmaker.media.domain.api.FavouriteTracksRepository
import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.domain.impl.FavouriteTracksInteractorImpl
import com.example.playlistmaker.media.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.media.ui.viewModel.FavouriteTracksViewModel
import com.example.playlistmaker.media.ui.viewModel.NewPlaylistViewModel
import com.example.playlistmaker.media.ui.viewModel.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule =  module {

    viewModel {
        FavouriteTracksViewModel(favouriteTracksInteractor = get())
    }

    viewModel {
        NewPlaylistViewModel(playlistInteractor = get())
    }

    viewModel {
        PlayListViewModel(playlistInteractor = get())
    }

    factory {
        DbConverter()
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(appDatabase = get(), trackDbConvertor = get())
    }

    single<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(favouriteTracksRepository = get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(appDatabase = get(), dbConvertor = get(), localStorage = get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(playlistRepository = get())
    }

    single<LocalStorage> {
        LocalStorageImpl(context = get())
    }
}


