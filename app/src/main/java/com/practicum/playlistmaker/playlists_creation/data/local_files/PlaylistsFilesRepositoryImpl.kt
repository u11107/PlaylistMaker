package com.practicum.playlistmaker.playlists_creation.data.local_files

import android.net.Uri
import com.practicum.playlistmaker.playlists_creation.domain.api.local_files.PlaylistsFilesRepository
import java.net.URI

class PlaylistsFilesRepositoryImpl(private val privateStorage: PrivateStorage) :
    PlaylistsFilesRepository {
    override suspend fun addToPrivateStorage(uri: Uri): URI {
        return privateStorage.saveImage(uri)
    }
}