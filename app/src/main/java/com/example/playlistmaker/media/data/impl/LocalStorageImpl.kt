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
    companion object {
        private const val QUALITY_IMAGE = 30
        private const val DIRECTORY = "playlist"
        private const val IMAGE_NAME = "image"
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        withContext(Dispatchers.IO) {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)

            if (!filePath.exists()) {
                filePath.mkdirs()
            }

            val imageNumber = System.currentTimeMillis()
            val imageName = "$IMAGE_NAME$imageNumber.jpg"
            val file = File(filePath, imageName)
            val inputStream = context.contentResolver.openInputStream(uri)

            try {
                inputStream?.use { input ->
                    val bitmap = BitmapFactory.decodeStream(input)
                    val outputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_IMAGE, outputStream)
                    outputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}










