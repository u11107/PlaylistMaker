package com.example.playlistmaker.di

import com.example.playlistmaker.media.viewmodel.FavoritesTracksViewModel
import com.example.playlistmaker.media.viewmodel.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    viewModel{
        FavoritesTracksViewModel()
    }

    viewModel{
        PlayListViewModel()
    }
}

