package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist): Long

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun getPlaylistById(id: Long): Playlist?

    suspend fun getPlaylists(): List<Playlist>

    suspend fun getFlowPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlistId: Long)

    suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long)

    suspend fun getFlowPlaylistById(id: Long): Flow<Playlist?>

    suspend fun getPlaylistTracks(): List<Track>

    suspend fun getPlaylistTracksByTrackIdList(trackIdList: List<Long>): List<Track>
}