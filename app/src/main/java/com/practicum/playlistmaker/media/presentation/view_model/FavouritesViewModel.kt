package com.practicum.playlistmaker.media.presentation.view_model

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.FavouritesInteractor
import com.practicum.playlistmaker.media.presentation.models.FavouritesScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class FavouritesViewModel(private val favouritesInteractor: FavouritesInteractor) : ViewModel(),
    DefaultLifecycleObserver {

    private val stateLiveData = MutableLiveData<FavouritesScreenState>()
    fun observeState(): LiveData<FavouritesScreenState> = stateLiveData

    private val showPlayerTrigger = SingleEventLiveData<Track>()
    fun getShowPlayerTrigger(): LiveData<Track> = showPlayerTrigger

    private var isClickAllowed = true
    private val onTrackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickAllowed = it
        }

    init {
        loadContent()
    }

    private fun setState(state: FavouritesScreenState) {
        stateLiveData.postValue(state)
    }

    private fun loadContent() {
        setState(FavouritesScreenState.Loading)
        viewModelScope.launch {
            favouritesInteractor
                .getFavouriteTracks()
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            setState(FavouritesScreenState.Empty)
        } else {
            setState(FavouritesScreenState.Content(tracks))
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    fun showPlayer(track: Track) {
        if (clickDebounce()) {
            showPlayerTrigger.value = track
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}