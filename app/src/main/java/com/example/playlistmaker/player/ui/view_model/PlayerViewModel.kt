package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.api.FavouriteTracksInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.domain.interactor.PlayerInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.App.Companion.formatTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private var isFavourite = false

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = timeLiveData

    private val isFavouriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavourite(): LiveData<Boolean> = isFavouriteLiveData

    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) timerJob?.cancel()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(DELAY_TIME_MILLIS)
                timeLiveData.postValue(playerInteractor.getPosition().formatTime())
            }
        }
    }

    fun prepare(url: String) {
        timerJob?.cancel()
        playerInteractor.preparePlayer(url)
    }

    fun play() {
        playerInteractor.startPlayer()
        startTimer()
    }

    fun pause() {
        playerInteractor.pausePlayer()
        timerJob?.cancel()
    }

    fun reset() {
        playerInteractor.reset()
        timerJob?.cancel()
    }

    fun checkIsFavourite(trackId: Int) {
        viewModelScope.launch {
            favouriteTracksInteractor
                .isFavoriteTrack(trackId)
                .collect { isFavorite ->
                    isFavourite = isFavorite
                    isFavouriteLiveData.postValue(isFavourite)
                }
        }
    }

    fun onFavouriteClicked(track: Track) {
        viewModelScope.launch {
            isFavourite = if (isFavourite) {
                favouriteTracksInteractor.deleteFromFavorites(track.trackId)
                isFavouriteLiveData.postValue(false)
                false
            } else {
                favouriteTracksInteractor.addToFavorites(track)
                isFavouriteLiveData.postValue(true)
                true
            }
        }
    }

    companion object {
        const val DELAY_TIME_MILLIS = 300L
    }
}