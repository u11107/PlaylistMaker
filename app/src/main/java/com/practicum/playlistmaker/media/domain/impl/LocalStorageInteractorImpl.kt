package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.LocalStorageInteractor
import java.io.File

class LocalStorageInteractorImpl(private val fileDir: File) : LocalStorageInteractor {
    override fun getImageDirectory() = fileDir
}