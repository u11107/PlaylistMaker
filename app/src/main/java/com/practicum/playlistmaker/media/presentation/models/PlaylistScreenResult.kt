package com.practicum.playlistmaker.media.presentation.models

import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistScreenResult {
    object Canceled : PlaylistScreenResult

    data class Created(val content: Playlist) : PlaylistScreenResult
}