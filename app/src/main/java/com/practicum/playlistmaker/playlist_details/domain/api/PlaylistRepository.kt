package com.practicum.playlistmaker.playlist_details.domain.api

import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

interface PlaylistRepository {
    suspend fun deletePlaylist(playlistId: Int)
    suspend fun getPlaylist(playlistId: Int): Playlist
    suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int)
    suspend fun getTracksInPlaylist(playlist: Playlist): List<Track>
}