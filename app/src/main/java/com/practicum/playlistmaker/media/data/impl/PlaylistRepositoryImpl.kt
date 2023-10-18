package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.AppDatabase
import com.practicum.playlistmaker.media.data.mapper.PlaylistDbMapper
import com.practicum.playlistmaker.media.data.mapper.PlaylistTrackDbMapper
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbMapper: PlaylistDbMapper,
    private val playlistTrackDbMapper: PlaylistTrackDbMapper
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist): Long {
        val playlistEntity = playlistDbMapper.map(playlist)
        return try {
            val result = appDatabase.playlistDao().insertPlaylist(playlistEntity)
            result
        } catch (e: Exception) {
            0
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val tracksIdList  = playlist.trackList.toList()
        val entity = playlistDbMapper.map(playlist)
        appDatabase.playlistDao().deletePlaylist(entity)
        tracksIdList.forEach { trackId ->
            if (isUnusedPlaylistTrack(trackId)) {
                val track = appDatabase.playlistTrackDao().getPlaylistTrackById(trackId)
                appDatabase.playlistTrackDao().deletePlaylistTrack(track)
            }
        }
    }

    override suspend fun getPlaylistById(id: Long): Playlist? {
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(id)
        return if (playlistEntity != null) {
            playlistDbMapper.map(playlistEntity)
        } else {
            null
        }
    }

    override suspend fun getPlaylists(): List<Playlist> {
        return appDatabase.playlistDao().getPlaylists().map { playlistEntity -> playlistDbMapper.map(playlistEntity) }
    }

    override suspend fun getFlowPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getFlowPlaylists().map { it.map { playlistEntity -> playlistDbMapper.map(playlistEntity) } }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        val playlist = getPlaylistById(playlistId)
        if (playlist != null) {
            appDatabase.playlistTrackDao().insertPlaylistTrack(playlistTrackDbMapper.map(track))
            playlist.trackList.add(track.trackId!!)
            playlist.trackCount +=1
            appDatabase.playlistDao().updatePlaylist(playlistDbMapper.map(playlist))
        }

    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long) {
        val playlist = getPlaylistById(playlistId)
        if (playlist != null) {
            playlist.trackList.remove(track.trackId)
            playlist.trackCount -= 1
            appDatabase.playlistDao().updatePlaylist(playlistDbMapper.map(playlist))
            val trackId = track.trackId ?: 0
            if (trackId > 0 && isUnusedPlaylistTrack(trackId)) {
                appDatabase.playlistTrackDao().deletePlaylistTrack(playlistTrackDbMapper.map(track))
            }
        }

    }

    override suspend fun getFlowPlaylistById(id: Long): Flow<Playlist?> {
        val flowPlaylistEntity = appDatabase.playlistDao().getFlowPlaylistById(id)
        return (flowPlaylistEntity.map { playlistEntity -> if (playlistEntity != null) playlistDbMapper.map(playlistEntity) else null })
    }

    override suspend fun getPlaylistTracks(): List<Track> {
        return appDatabase.playlistTrackDao().getPlaylistTracks().map { playlistTrackEntity -> playlistTrackDbMapper.map(playlistTrackEntity)}
    }


    override suspend fun getPlaylistTracksByTrackIdList(trackIdList: List<Long>): List<Track> {
        return if (trackIdList.isEmpty())
            listOf()
        else {
            val allPlaylistTracks = getPlaylistTracks()
            val playlistTracks = allPlaylistTracks.filter { trackIdList.indexOf(it.trackId) > -1 }.associateBy { it.trackId }
            val sortedPlaylistTracks = trackIdList.map { playlistTracks[it] }
            (sortedPlaylistTracks) as List<Track>
        }
    }

    private suspend fun isUnusedPlaylistTrack(trackId: Long): Boolean {
        val playlists = getPlaylists().filter { playlist -> playlist.trackList.indexOf(trackId) > -1 }
        return playlists.isEmpty()
    }
}