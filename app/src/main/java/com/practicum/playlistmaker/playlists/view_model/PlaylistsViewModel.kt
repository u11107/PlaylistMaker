package com.practicum.playlistmaker.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlists_creation.domain.api.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.playlists_creation.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsDbInteractor) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<PlaylistsState>()
    fun observePlaylists(): LiveData<PlaylistsState> = playlistsLiveData

    fun displayState() {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch {
            interactor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) playlistsLiveData.postValue(PlaylistsState.DisplayEmptyPlaylists)
        else playlistsLiveData.postValue(PlaylistsState.DisplayPlaylists(playlists))
    }

}