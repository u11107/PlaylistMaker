package com.practicum.playlistmaker.media.presentation.models

import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistDetailsScreenState {

    object Loading : PlaylistDetailsScreenState

    data class Error(val message: String) : PlaylistDetailsScreenState

    data class Content(val data: Playlist) : PlaylistDetailsScreenState
}