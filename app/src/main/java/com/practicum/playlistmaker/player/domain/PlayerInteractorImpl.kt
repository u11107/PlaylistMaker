package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.player.domain.api.Player
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor

class PlayerInteractorImpl(private val player: Player) : PlayerInteractor {

    override fun preparePlayer(trackUri: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        player.preparePlayer(trackUri, onPrepared, onCompletion)
    }

    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun getPlayerPosition() = player.getPlayerPosition()

    override fun releasePlayer() {
        player.releasePlayer()
    }

    override fun isPlaying() = player.isPlaying()
}