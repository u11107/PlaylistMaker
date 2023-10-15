package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorites.view_model.FavoritesViewModel
import com.practicum.playlistmaker.playlists.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.playlist_details.view_model.PlaylistDetailsViewModel
import com.practicum.playlistmaker.playlist_edit.view_model.PlaylistEditViewModel
import com.practicum.playlistmaker.playlist_creation.view_model.PlaylistCreationViewModel
import com.practicum.playlistmaker.search.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::PlaylistCreationViewModel)
    viewModelOf(::PlaylistsViewModel)
    viewModelOf(::PlaylistDetailsViewModel)
    viewModelOf(::PlaylistEditViewModel)
}