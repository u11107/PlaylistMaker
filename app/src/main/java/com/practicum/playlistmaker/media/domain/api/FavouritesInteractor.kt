package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {

    suspend fun getFavouriteTracks(): Flow<List<Track>>

    suspend fun saveFavouriteTrack(track: Track)

    suspend fun deleteFavouriteTrack(trackId: Long)

    suspend fun getFavouriteState(trackId: Long): Boolean
}