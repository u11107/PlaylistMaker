package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.ui.model.PlayerState

class PlayerInteractorImpl(private val repository: PlayerRepository):PlayerInteractor {

    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun release() {
        repository.release()
    }

    override fun getPosition(): Long {
       return repository.getPosition()
    }

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        repository.setOnStateChangeListener(callback)
    }
}