package com.practicum.playlistmaker.favorites.view_model

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FavoritesState {
    object EmptyFavorites : FavoritesState
    class DisplayFavorites(val tracks: List<Track>) : FavoritesState
}