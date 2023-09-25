package com.example.playlistmaker.media.ui.model

import com.example.playlistmaker.search.domain.model.Track

sealed interface FavouriteTracksState {
    data class Content(
        val tracks: List<Track>
    ) : FavouriteTracksState

    object Empty : FavouriteTracksState
}