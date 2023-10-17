package com.practicum.playlistmaker.media.presentation.models

import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistsScreenState {

    object Loading : PlaylistsScreenState

    object Empty : PlaylistsScreenState

    data class Content(val playlists: List<Playlist>) : PlaylistsScreenState
}