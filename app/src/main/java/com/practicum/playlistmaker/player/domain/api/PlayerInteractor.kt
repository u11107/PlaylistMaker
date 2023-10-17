package com.practicum.playlistmaker.player.domain.api

interface PlayerInteractor {
    fun preparePlayer(
        previewUrl: String?,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
        onError: () -> Unit
    )

    fun startPlayer(onStart: () -> Unit)
    fun pausePlayer(onPause: () -> Unit)
    fun playbackControl(onStart: () -> Unit, onPause: () -> Unit)
    fun getCurrentPosition(default: String?): String?
    fun release()
}