package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayerState
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository) :
    PlayerInteractor {
    private var playerState = PlayerState.DEFAULT

    override fun preparePlayer(
        previewUrl: String?,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
        onError: () -> Unit
    ) {
        mediaPlayerRepository.setOnPreparedListener {
            onPreparedListener()
            playerState = PlayerState.PREPARED
        }
        try {
            mediaPlayerRepository.prepare(previewUrl.toString())
        } catch (e: Exception) {
            onError()
            return
        }
        mediaPlayerRepository.setOnCompletionListener {
            onCompletionListener()
            playerState = PlayerState.PREPARED
        }
    }

    override fun startPlayer(onStart: () -> Unit) {
        if (playerState != PlayerState.DEFAULT) {
            mediaPlayerRepository.start()
            playerState = PlayerState.PLAYING
            onStart()
        }
    }

    override fun pausePlayer(onPause: () -> Unit) {
        if (playerState != PlayerState.DEFAULT) {
            onPause()
            mediaPlayerRepository.pause()
            playerState = PlayerState.PAUSED
        }
    }

    override fun playbackControl(onStart: () -> Unit, onPause: () -> Unit) {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer(onPause)
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer(onStart)
            }

            else -> {}
        }
    }

    override fun getCurrentPosition(default: String?): String? {
        return when (playerState) {
            PlayerState.PLAYING -> SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                mediaPlayerRepository.getCurrentPosition()
            )

            PlayerState.PREPARED, PlayerState.DEFAULT -> default
            else -> null
        }
    }

    override fun release() {
        mediaPlayerRepository.release()
    }
}