package com.practicum.playlistmaker.media.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.LocalStorageInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.models.PlaylistDetailsScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.utils.debounce
import com.practicum.playlistmaker.utils.getMinuteCountNoun
import com.practicum.playlistmaker.utils.getTrackCountNoun
import kotlinx.coroutines.launch
import java.io.File

class PlaylistDetailsViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val localStorageInteractor: LocalStorageInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistDetailsScreenState>()
    fun observeState(): LiveData<PlaylistDetailsScreenState> = stateLiveData

    private val trackListLiveData = MutableLiveData<List<Track>>()
    fun observeTrackList(): LiveData<List<Track>> = trackListLiveData

    private val showPlayerTrigger = SingleEventLiveData<Track>()
    fun getShowPlayerTrigger(): LiveData<Track> = showPlayerTrigger

    private val showPlaylistEditTrigger = SingleEventLiveData<Playlist>()
    fun getShowPlaylistEditTrigger(): LiveData<Playlist> = showPlaylistEditTrigger

    private val deletePlaylistTrigger = SingleEventLiveData<Boolean>()
    fun getDeletePlaylistTrigger(): LiveData<Boolean> = deletePlaylistTrigger

    private var isClickAllowed = true
    private val onTrackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickAllowed = it
        }

    init {
        loadContent()
    }

    private fun loadContent() {
        setState(PlaylistDetailsScreenState.Loading)

        viewModelScope.launch {
            playlistInteractor
                .getFlowPlaylistById(playlistId)
                .collect { playlist ->
                    processResult(playlist)
                    if (playlist != null) {
                        setTrackList(playlistInteractor.getPlaylistTracksByTrackIdList(playlist.trackList))
                    }
                }
        }
    }

    private fun setState(state: PlaylistDetailsScreenState) {
        stateLiveData.value = state
    }

    private fun setTrackList(trackList: List<Track>) {
        trackListLiveData.value = trackList
    }

    private fun processResult(playlist: Playlist?) {
        if (playlist == null) {
            setState(PlaylistDetailsScreenState.Error("Плейлист не найден"))
        } else {
            setState(PlaylistDetailsScreenState.Content(playlist))
        }
    }

    private fun getTrackCount(): Int = trackListLiveData.value?.size ?: 0

    private fun getPlaylistTimeMillis(): Long = trackListLiveData.value?.sumOf { it.trackTimeMillis ?: 0 } ?: 0

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    fun getImageDirectory(): File = localStorageInteractor.getImageDirectory()

    fun getTrackCountStatistics(): String {
        val trackCount = getTrackCount()
        return trackCount.toString() + " " + getTrackCountNoun(trackCount)
    }

    fun getPlaylistTimeStatistics(): String {
        val minutes = getPlaylistTimeMillis() / 1000 / 60
        return minutes.toString() + " " + getMinuteCountNoun(minutes)
    }

    fun showPlayer(track: Track) {
        if (clickDebounce()) {
            showPlayerTrigger.value = track
        }
    }

    fun onDeleteTrackClick(track: Track) {
        if (stateLiveData.value is PlaylistDetailsScreenState.Content) {
            viewModelScope.launch {
                val playlistId = (stateLiveData.value as PlaylistDetailsScreenState.Content).data.id
                playlistInteractor.deleteTrackFromPlaylist(track, playlistId)
            }
        }
    }

    fun onSharePlaylist(): Boolean {
        return if (trackListLiveData.value.isNullOrEmpty() or (stateLiveData.value !is PlaylistDetailsScreenState.Content))
            false
        else {
            val playlist = (stateLiveData.value as PlaylistDetailsScreenState.Content).data
            val trackList = trackListLiveData.value.orEmpty()
            val playlistInfo = playlistInteractor.getPlaylistInfo(playlist, trackList)
            playlistInteractor.sharePlaylist(playlistInfo)
            true
        }
    }

    fun onDeletePlaylist() {
        Log.e("sprint23", "deleting.. ")
        if (stateLiveData.value is PlaylistDetailsScreenState.Content) {
            viewModelScope.launch {

                val playlist = (stateLiveData.value as PlaylistDetailsScreenState.Content).data
                playlistInteractor.deletePlaylist(playlist)
                deletePlaylistTrigger.value = true
            }
        }
    }

    fun showPlaylistEdit() {
        if (clickDebounce() && stateLiveData.value is PlaylistDetailsScreenState.Content) {
            showPlaylistEditTrigger.value = (stateLiveData.value as PlaylistDetailsScreenState.Content).data
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}