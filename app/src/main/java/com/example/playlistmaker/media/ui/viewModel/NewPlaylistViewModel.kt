package com.example.playlistmaker.media.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import com.example.playlistmaker.media.ui.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist)
        }
    }


    fun saveToLocalStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.saveImageToPrivateStorage(uri)
        }
    }
}