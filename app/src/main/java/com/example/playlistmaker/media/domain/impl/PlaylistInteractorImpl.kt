package com.example.playlistmaker.media.domain.impl

import android.net.Uri
import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

//    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean> =
//        playlistRepository.addTrackToPlayList(track, playlist)

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean> =
        flow {
            val gson = GsonBuilder().create()
            val arrayTrackType = object : TypeToken<ArrayList<Long>>() {}.type

            val playlistTracks =
                gson.fromJson(playlist.trackList, arrayTrackType) ?: arrayListOf<Long>()

            if (!playlistTracks.contains(track.trackId.toLong())) {
                playlistTracks.add(track.trackId.toLong())
                playlist.trackList = gson.toJson(playlistTracks)

                playlist.size++
                updatePlaylists(playlist)

                emit(true)
            } else {
                emit(false)
            }
        }


    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        playlistRepository.saveImageToPrivateStorage(uri)
    }
}