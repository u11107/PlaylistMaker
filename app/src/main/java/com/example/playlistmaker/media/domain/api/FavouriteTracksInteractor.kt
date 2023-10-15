package com.example.playlistmaker.media.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {
    suspend fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean>

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(trackId: Int)
}