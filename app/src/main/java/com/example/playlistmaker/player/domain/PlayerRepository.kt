package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.ui.model.PlayerState

interface PlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}