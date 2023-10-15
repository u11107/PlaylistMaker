package com.practicum.playlistmaker.playlists_creation.data.local_files

import android.net.Uri
import java.net.URI

interface PrivateStorage {
    suspend fun saveImage(uri: Uri): URI
}