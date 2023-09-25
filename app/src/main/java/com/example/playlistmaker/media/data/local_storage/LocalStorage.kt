package com.example.playlistmaker.media.data.local_storage

import android.net.Uri

interface LocalStorage {
    suspend fun saveImageToPrivateStorage(uri: Uri)
}