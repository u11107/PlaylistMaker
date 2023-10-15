package com.practicum.playlistmaker.playlist_creation.view_model

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.playlist_creation.domain.api.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesInteractor
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class PlaylistCreationViewModel(
    private val dbInteractor: PlaylistsDbInteractor,
    private val filesInteractor: PlaylistsFilesInteractor,
    private val requester: PermissionRequester
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<PlaylistCreationState>()
    fun observeScreenState(): LiveData<PlaylistCreationState> = screenStateLiveData

    private val permissionStateLiveDate = MutableLiveData<PermissionState>()
    fun observePermissionState(): LiveData<PermissionState> = permissionStateLiveDate

    private val createButtonStateLiveData = MutableLiveData<CreateButtonState>()
    fun observeCreateButtonState(): LiveData<CreateButtonState> = createButtonStateLiveData

    protected open var playlist = Playlist()
    protected open var coverUri: String? = null
    protected open var name: String? = null
    protected open var description: String? = null

    fun onCoverClicked() {
        handlePermission()
    }

    private fun handlePermission() {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requester.request(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                requester.request(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.collect { result ->
                when (result) {
                    is PermissionResult.Granted -> permissionStateLiveDate.postValue(PermissionState.GRANTED)
                    is PermissionResult.Denied.DeniedPermanently ->
                        permissionStateLiveDate.postValue(PermissionState.DENIED_PERMANENTLY)

                    else -> permissionStateLiveDate.postValue(PermissionState.NEEDS_RATIONALE)
                }
            }
        }
    }

    open fun onNameChanged(text: CharSequence?) {
        if (text.toString().isEmpty())
            createButtonStateLiveData.value = CreateButtonState.DISABLED
        else createButtonStateLiveData.value = CreateButtonState.ENABLED
        if (text != null) {
            name = text.toString()
        }
    }

    open fun onDescriptionChanged(text: CharSequence?) {
        if (text != null) {
            description = text.toString()
        }
    }

    open fun onCreatePlClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!coverUri.isNullOrEmpty()) {
                coverUri = filesInteractor.addToPrivateStorage(Uri.parse(coverUri)).toString()
            }
            dbInteractor.addPlaylist(
                playlist.copy(
                    name = name ?: "",
                    description = description ?: "",
                    coverUri = coverUri ?: ""
                )
            )
        }
        viewModelScope.launch {
            screenStateLiveData.value = PlaylistCreationState.PLAYLIST_CREATED
        }
    }

    fun saveCoverUri(uri: Uri?) {
        coverUri = uri.toString()
    }

    fun onBackPressed() {
        if (!coverUri.isNullOrEmpty() || !name.isNullOrEmpty() || !description.isNullOrEmpty()) {
            screenStateLiveData.value = PlaylistCreationState.SHOW_DIALOG
        } else {
            screenStateLiveData.value = PlaylistCreationState.EMPTY_STATE
        }
    }
}