package com.practicum.playlistmaker.media.presentation.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavouritesScreenState {

    object Loading : FavouritesScreenState

    object Empty : FavouritesScreenState

    data class Content(val tracks: List<Track>) : FavouritesScreenState
}