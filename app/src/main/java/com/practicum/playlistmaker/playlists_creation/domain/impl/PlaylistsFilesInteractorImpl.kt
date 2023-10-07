package com.practicum.playlistmaker.playlists_creation.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.playlists_creation.domain.api.local_files.PlaylistsFilesInteractor
import com.practicum.playlistmaker.playlists_creation.domain.api.local_files.PlaylistsFilesRepository
import java.net.URI

class PlaylistsFilesInteractorImpl(private val filesRepository: PlaylistsFilesRepository) :
    PlaylistsFilesInteractor {
    override suspend fun addToPrivateStorage(uri: Uri): URI {
        return filesRepository.addToPrivateStorage(uri)
    }
}