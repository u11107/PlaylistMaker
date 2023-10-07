package com.practicum.playlistmaker.favorites.domain.impl

import com.practicum.playlistmaker.favorites.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.favorites.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {

    override suspend fun addFavorite(track: Track) {
        repository.addFavorite(track)
    }

    override suspend fun deleteFavorite(track: Track) {
        repository.deleteFavorite(track)
    }

    override fun getFavorites(): Flow<List<Track>> = repository.getFavorites()

}