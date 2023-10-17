package com.practicum.playlistmaker.media.presentation.view_model

import com.practicum.playlistmaker.media.domain.api.LocalStorageInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import java.io.File

class PlaylistEditViewModel(
    playlist: Playlist,
    playlistInteractor: PlaylistInteractor,
    private val localStorageInteractor: LocalStorageInteractor
) : PlaylistViewModel(playlistInteractor, localStorageInteractor) {

    init {
        setPlaylistValue(playlist)
    }

    fun getImageDirectory(): File = localStorageInteractor.getImageDirectory()

    override fun needShowDialog() = false
}