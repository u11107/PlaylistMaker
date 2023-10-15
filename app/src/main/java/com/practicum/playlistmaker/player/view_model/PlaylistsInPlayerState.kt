package com.practicum.playlistmaker.player.view_model

import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist

sealed interface PlaylistsInPlayerState {
    class DisplayPlaylists(val playlists: List<Playlist>) : PlaylistsInPlayerState
    object HidePlaylists : PlaylistsInPlayerState
}