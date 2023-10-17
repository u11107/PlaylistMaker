package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayer : MediaPlayer) : MediaPlayerRepository {

    override fun prepare(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun setOnPreparedListener(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
    }
}