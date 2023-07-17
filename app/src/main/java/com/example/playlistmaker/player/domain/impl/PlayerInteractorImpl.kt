package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.interactor.PlayerInteractor
import com.example.playlistmaker.player.domain.repository.PlayerRepository
import com.example.playlistmaker.player.domain.PlayerState

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {

    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun reset() {
        repository.reset()
    }

    override fun getPosition(): Long {
       return repository.getPosition()
    }

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        repository.setOnStateChangeListener(callback)
    }
}