package com.practicum.playlistmaker.playlist_creation.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesInteractor
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesRepository
import java.net.URI

class PlaylistsFilesInteractorImpl(private val filesRepository: PlaylistsFilesRepository) :
    PlaylistsFilesInteractor {

    override suspend fun addToPrivateStorage(uri: Uri): URI = filesRepository.addToPrivateStorage(uri)

    override suspend fun deleteFromPrivateStorage(coverUri: String) {
        filesRepository.deleteFromPrivateStorage(coverUri)
    }
}