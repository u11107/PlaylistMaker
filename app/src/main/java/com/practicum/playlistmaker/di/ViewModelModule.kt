package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorites.view_model.FavoritesViewModel
import com.practicum.playlistmaker.playlists.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.playlists_creation.view_model.PlaylistsCreationViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsCreationViewModel(get(), get(), get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }
}