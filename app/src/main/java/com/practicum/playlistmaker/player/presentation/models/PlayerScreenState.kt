package com.practicum.playlistmaker.player.presentation.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface PlayerScreenState {
    data class Default(val track: Track) : PlayerScreenState

    object Prepared : PlayerScreenState

    data class Playing(val time: String? = null) : PlayerScreenState

    data class Progress(val time: String? = null) : PlayerScreenState

    data class Paused(val time: String? = null) : PlayerScreenState
}