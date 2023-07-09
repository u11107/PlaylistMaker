package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.PlayerState

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}