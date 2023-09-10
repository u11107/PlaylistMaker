package com.example.playlistmaker.di

import com.example.playlistmaker.db.TrackDbConverter
import com.example.playlistmaker.media.data.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.media.domain.api.FavouriteTracksInteractor
import com.example.playlistmaker.media.domain.api.FavouriteTracksRepository
import com.example.playlistmaker.media.domain.impl.FavouriteTracksInteractorImpl
import com.example.playlistmaker.media.ui.viewModel.FavouriteTracksViewModel
import com.example.playlistmaker.media.ui.viewModel.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    viewModel {
        FavouriteTracksViewModel(favouriteTracksInteractor = get())
    }

    viewModel {
        PlayListViewModel()
    }

    factory {
        TrackDbConverter()
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(appDatabase = get(), trackDbConvertor = get())
    }

    single<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(favouriteTracksRepository = get())
    }
}

