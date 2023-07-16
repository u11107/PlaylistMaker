package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.domain.interactor.PlayerInteractor
import com.example.playlistmaker.util.App.Companion.formatTime

class PlayerViewModel(val playerInteractor: PlayerInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = timeLiveData
    private val handler = Handler(Looper.getMainLooper())

    private val time = object : Runnable {
        override fun run() {
            val position = playerInteractor.getPosition()
            timeLiveData.postValue(position.formatTime())
            handler.postDelayed(this, DELAY_MILLIS)
        }
    }

    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) handler.removeCallbacks(time)
        }
    }


    fun prepare(url: String) {
        handler.removeCallbacks(time)
        playerInteractor.preparePlayer(url)
    }

    fun play() {
        playerInteractor.startPlayer()
        handler.post(time)
    }

    fun pause() {
        playerInteractor.pausePlayer()
        handler.removeCallbacks(time)
    }

    fun release() {
        playerInteractor.reset()
        handler.removeCallbacks(time)
    }

    companion object {
        private const val DELAY_MILLIS = 1000L
    }
}