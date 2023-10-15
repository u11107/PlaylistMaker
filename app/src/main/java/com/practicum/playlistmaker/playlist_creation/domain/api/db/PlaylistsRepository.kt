package com.practicum.playlistmaker.playlist_creation.domain.api.db

import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylist(playlistId: Int): Playlist
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}