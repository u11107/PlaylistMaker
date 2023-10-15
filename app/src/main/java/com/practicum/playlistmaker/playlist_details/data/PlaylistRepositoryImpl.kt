package com.practicum.playlistmaker.playlist_details.data

import com.practicum.playlistmaker.favorites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.playlist_details.domain.api.PlaylistRepository
import com.practicum.playlistmaker.playlist_creation.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val database: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {

    override suspend fun getTracksInPlaylist(
        playlist: Playlist
    ): List<Track> =
        withContext(Dispatchers.IO) {
            val tracksIds =
                database.playlistsTracksCrossRefDao().getTracksInPlaylist(playlist.playlistId)
                    .sortedByDescending { it.addingTime }.map { crossRef -> crossRef.trackId }
            database.tracksInPlDao().getTracks(tracksIds).sortedBy { tracksIds.indexOf(it.trackId) }
                .map { trackInPlEntity ->
                    trackDbConverter.map(
                        trackInPlEntity,
                        checkIsFavorite(trackInPlEntity.trackId)
                    )
                }
        }

    private suspend fun checkIsFavorite(trackId: Int): Boolean =
        withContext(Dispatchers.IO) {
            database.favoritesDao().isInFavorite(trackId)
        }

    override suspend fun getPlaylist(playlistId: Int): Playlist =
        playlistDbConverter.map(database.playlistsDao().getPlaylist(playlistId))

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        database.playlistsTracksCrossRefDao()
            .deleteTrack(trackId, playlistId)
        val updatedNumberOfTracks = database.playlistsDao().getNumberOfTracks(playlistId) - 1
        database.playlistsDao().updateNumberOfTracks(playlistId, updatedNumberOfTracks)
        checkAndDeleteTrack(trackId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        database.playlistsDao().deletePlaylist(playlistId)
        val tracksInPlaylist =
            database.playlistsTracksCrossRefDao().getTracksInPlaylist(playlistId)
        database.playlistsTracksCrossRefDao().deleteAllTracksInPlaylist(playlistId)
        for (track in tracksInPlaylist) {
            checkAndDeleteTrack(track.trackId)
        }
    }

    private suspend fun checkAndDeleteTrack(trackId: Int) {
        if (database.playlistsTracksCrossRefDao().getPlaylistsContainingTrack(trackId)
                .isEmpty()
        ) database.tracksInPlDao().deleteTrack(trackId)
    }
}