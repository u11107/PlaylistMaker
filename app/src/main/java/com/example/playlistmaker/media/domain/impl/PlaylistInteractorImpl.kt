package com.example.playlistmaker.media.domain.impl

import android.net.Uri
import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(id: Int) {
        playlistRepository.deletePlaylist(id)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updatePlaylists(playlist: Playlist) {
        playlistRepository.updatePlaylists(playlist)
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean> =
        playlistRepository.addTrackToPlayList(track, playlist)

    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        playlistRepository.saveImageToPrivateStorage(uri)
    }
}