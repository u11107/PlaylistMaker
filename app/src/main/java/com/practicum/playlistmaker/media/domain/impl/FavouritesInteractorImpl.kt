package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.FavouritesInteractor
import com.practicum.playlistmaker.media.domain.api.FavouritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouritesInteractorImpl(private val favouritesRepository: FavouritesRepository): FavouritesInteractor {
    override suspend fun getFavouriteTracks(): Flow<List<Track>> {
        return favouritesRepository.getFavouriteTracks()
    }

    override suspend fun saveFavouriteTrack(track: Track) {
        favouritesRepository.saveFavouriteTrack(track)
    }

    override suspend fun deleteFavouriteTrack(trackId: Long) {
        favouritesRepository.deleteFavouriteTrack(trackId)
    }

    override suspend fun getFavouriteState(trackId: Long): Boolean {
        return favouritesRepository.getFavouriteState(trackId)
    }
}