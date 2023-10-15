package com.practicum.playlistmaker.playlist_creation.data.db

import com.practicum.playlistmaker.favorites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.playlist_creation.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.playlist_creation.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.playlist_creation.data.db.entity.PlaylistTracksCrossRef
import com.practicum.playlistmaker.playlist_creation.domain.api.db.PlaylistsRepository
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Calendar

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        database.playlistsDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = convertFromPlaylistEntity(database.playlistsDao().getPlaylists())
        for (playlist in playlists) {
            playlist.tracks.addAll(
                convertFromTracksInPlaylists(
                    database.playlistsTracksCrossRefDao().getTracksInPlaylist(playlist.playlistId)
                )
            )
        }
        emit(playlists)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        database.playlistsDao().updateNumberOfTracks(
            playlist.playlistId,
            playlist.numberOfTracks
        )
        database.tracksInPlDao().addTrackToPlaylist(trackDbConverter.map(track))
        database.playlistsTracksCrossRefDao()
            .addTrackToPlaylist(
                PlaylistTracksCrossRef(
                    track.trackId,
                    playlist.playlistId,
                    Calendar.getInstance().time.time
                )
            )
    }

    override suspend fun getPlaylist(playlistId: Int): Playlist = withContext(Dispatchers.IO) {
        playlistDbConverter.map(database.playlistsDao().getPlaylist(playlistId))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> =
        playlists.map { playlist -> playlistDbConverter.map(playlist) }

    private fun convertFromTracksInPlaylists(tracksInPlaylists: List<PlaylistTracksCrossRef>): List<Int> =
        tracksInPlaylists.map { track -> track.trackId }
}