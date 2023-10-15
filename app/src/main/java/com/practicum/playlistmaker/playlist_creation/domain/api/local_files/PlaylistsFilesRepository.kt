package com.practicum.playlistmaker.playlist_creation.domain.api.local_files

import android.net.Uri
import java.net.URI

interface PlaylistsFilesRepository {
    suspend fun addToPrivateStorage(uri: Uri): URI
    suspend fun deleteFromPrivateStorage(coverUri: String)
}