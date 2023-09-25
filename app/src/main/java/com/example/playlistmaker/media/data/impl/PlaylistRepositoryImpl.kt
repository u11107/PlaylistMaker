package com.example.playlistmaker.media.data.impl

import android.net.Uri
import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.db.convertor.DbConverter
import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.media.data.local_storage.LocalStorage
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class   PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConverter,
    private val localStorage: LocalStorage,
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.PlaylistDao()
            .addPlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(id: Int) {
        appDatabase.PlaylistDao().deletePlaylist(id)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.PlaylistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updatePlaylists(playlist: Playlist) {
        appDatabase.PlaylistDao()
            .updatePlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

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
        localStorage.saveImageToPrivateStorage(uri)
    }


    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlists ->
            dbConvertor.mapFromPlaylistEntityToPlaylist(
                playlists
            )
        }
    }
}