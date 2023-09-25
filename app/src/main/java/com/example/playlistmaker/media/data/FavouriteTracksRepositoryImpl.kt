package com.example.playlistmaker.media.data

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.db.convertor.DbConverter
import com.example.playlistmaker.data.db.track.TrackDbConverter
import com.example.playlistmaker.data.db.track.TrackEntity
import com.example.playlistmaker.media.domain.api.FavouriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: DbConverter,
): FavouriteTracksRepository {

    override suspend fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.TrackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> = flow {
        val isFavorite = appDatabase.TrackDao().isFavoriteTrack(trackId)
        emit(isFavorite)
    }

    override suspend fun addToFavorites(track: Track) {
        appDatabase.TrackDao().addToFavorites(trackDbConvertor.mapFromTrackToTrackEntity(track))
    }

    override suspend fun deleteFromFavorites(trackId: Int) {
        appDatabase.TrackDao().deleteFromFavorites(trackId)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.mapFromTrackEntityToTrack(track) }
    }
}