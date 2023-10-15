package com.practicum.playlistmaker.playlists.view_model

import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist

sealed interface PlaylistsState {
    object DisplayEmptyPlaylists : PlaylistsState
    class DisplayPlaylists(val playlists: List<Playlist>) : PlaylistsState
}