package com.example.playlistmaker.media.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import com.example.playlistmaker.media.ui.model.Playlist
import kotlinx.coroutines.launch

class PlayListViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsScreenState>()
    val stateLiveData: LiveData<PlaylistsScreenState> = _stateLiveData

    init {
        _stateLiveData.postValue(PlaylistsScreenState.Empty)
    }

    fun fillData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _stateLiveData.postValue(PlaylistsScreenState.Empty)
        } else {
            _stateLiveData.postValue(PlaylistsScreenState.Filled(playlists))
        }
    }
}