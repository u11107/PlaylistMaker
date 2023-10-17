package com.practicum.playlistmaker.media.domain.api

import java.io.File

interface LocalStorageInteractor {
    fun getImageDirectory(): File
}