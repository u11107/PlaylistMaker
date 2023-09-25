package com.example.playlistmaker.media.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.media.data.local_storage.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class LocalStorageImpl(val context: Context) : LocalStorage {
    override suspend fun saveImageToPrivateStorage(uri: Uri) {

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)

        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, IMAGE_NAME)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY_IMAGE, outputStream)
    }

    companion object {
        private const val QUALITY_IMAGE = 30
        private const val DIRECTORY = "playlist"
        private const val IMAGE_NAME = "imageName"
    }
}