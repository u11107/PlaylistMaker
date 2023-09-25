package com.example.playlistmaker.media.domain.api

import android.net.Uri
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(id: Int)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylists(playlist: Playlist)

    suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean>

    suspend fun saveImageToPrivateStorage(uri: Uri)
}