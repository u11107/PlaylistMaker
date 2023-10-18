package com.practicum.playlistmaker.media.presentation.view_model

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.models.PlaylistsScreenState
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch


class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel(), DefaultLifecycleObserver {

    private val stateLiveData = MutableLiveData<PlaylistsScreenState>()
    fun observeState(): LiveData<PlaylistsScreenState> = stateLiveData

    private val showPlaylistDetailsTrigger = SingleEventLiveData<Long>()
    fun getShowPlaylistDetailsTrigger(): LiveData<Long> = showPlaylistDetailsTrigger

    private var isClickAllowed = true
    private val onPlaylistClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickAllowed = it
        }

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            playlistInteractor
                .getFlowPlaylists()
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            setState(PlaylistsScreenState.Empty)
        } else {
            // Сортируем плейлисты в порядке добавления (последний вверху)
            val sortedPlaylists = playlists.reversed()
            setState(PlaylistsScreenState.Content(sortedPlaylists))
        }
    }




    private fun setState(state: PlaylistsScreenState) {
        stateLiveData.postValue(state)
    }

    private fun playClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onPlaylistClickDebounce(true)
        }
        return current
    }

    fun showPlaylistDetails(playlistId: Long) {
        if (playClickDebounce()) {
            showPlaylistDetailsTrigger.value = playlistId
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}