package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.AppDatabase
import com.practicum.playlistmaker.media.data.mapper.TrackDbMapper
import com.practicum.playlistmaker.media.domain.api.FavouritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbMapper: TrackDbMapper,
) : FavouritesRepository {

    override suspend fun getFavouriteTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracks().map { it.map { trackEntity -> trackDbMapper.map(trackEntity) } }
    }

    override suspend fun getFavouriteState(trackId: Long): Boolean {
        return appDatabase.trackDao().getTrackIds().indexOf(trackId) > -1
    }

    override suspend fun saveFavouriteTrack(track: Track) {
        val trackEntity = trackDbMapper.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteFavouriteTrack(trackId: Long) {
        appDatabase.trackDao().deleteTrack(trackId)
    }
}