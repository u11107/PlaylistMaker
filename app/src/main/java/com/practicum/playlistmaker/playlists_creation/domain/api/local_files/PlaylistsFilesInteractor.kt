package com.practicum.playlistmaker.playlists_creation.domain.api.local_files

import android.net.Uri
import java.net.URI

interface PlaylistsFilesInteractor {
    suspend fun addToPrivateStorage(uri: Uri): URI
}