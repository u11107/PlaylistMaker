package com.practicum.playlistmaker.playlist_edit.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist_creation.domain.api.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesInteractor
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import com.practicum.playlistmaker.playlist_creation.view_model.PlaylistCreationViewModel
import com.practicum.playlistmaker.playlist_details.view_model.PlaylistDetails
import com.practicum.playlistmaker.utils.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistEditViewModel(
    private val dbInteractor: PlaylistsDbInteractor,
    filesInteractor: PlaylistsFilesInteractor,
    requester: PermissionRequester,
    private val playlistId: Int,
    private val resourceProvider: ResourceProvider
) : PlaylistCreationViewModel(dbInteractor, filesInteractor, requester) {

    private val playlistLiveData = MutableLiveData<PlaylistDetails>()
    private val playlistEditStringLiveData = MutableLiveData<PlaylistEditStringData>()

    override lateinit var playlist: Playlist

    fun observePlaylistData(): LiveData<PlaylistDetails> = playlistLiveData
    fun observePlaylistEditStringData(): LiveData<PlaylistEditStringData> =
        playlistEditStringLiveData

    fun initScreen() {
        viewModelScope.launch {
            playlist = getPlaylist(playlistId)
            playlistLiveData.value =
                PlaylistDetails(playlist.coverUri, playlist.name, playlist.description)
            playlistEditStringLiveData.value = PlaylistEditStringData(
                resourceProvider.getString(R.string.edit),
                resourceProvider.getString(R.string.save)
            )
            name = playlist.name
            description = playlist.description
            coverUri = playlist.coverUri
        }
    }

    private suspend fun getPlaylist(playlistId: Int): Playlist = withContext(Dispatchers.IO) {
        dbInteractor.getPlaylist(playlistId)
    }

    override fun onNameChanged(text: CharSequence?) {
        if (text != null) {
            playlist = playlist.copy(name = text.toString())
        }
        super.onNameChanged(text)
    }

    override fun onDescriptionChanged(text: CharSequence?) {
        if (text != null) {
            playlist = playlist.copy(description = text.toString())
        }
        super.onDescriptionChanged(text)
    }
}