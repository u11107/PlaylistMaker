package com.example.playlistmaker.media.ui.viewModel

import com.example.playlistmaker.media.ui.model.Playlist

sealed class PlaylistsScreenState(playlists: List<Playlist>?) {
    object Empty : PlaylistsScreenState(null)
    class Filled(val playlists: List<Playlist>) : PlaylistsScreenState(playlists)
}