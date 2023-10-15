package com.practicum.playlistmaker.playlist_creation.data.local_files

import android.net.Uri
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesRepository
import java.net.URI

class PlaylistsFilesRepositoryImpl(private val privateStorage: PrivateStorage) :
    PlaylistsFilesRepository {

    override suspend fun addToPrivateStorage(uri: Uri): URI = privateStorage.saveImage(uri)

    override suspend fun deleteFromPrivateStorage(coverUri: String) {
        privateStorage.deleteFromPrivateStorage(coverUri)
    }
}