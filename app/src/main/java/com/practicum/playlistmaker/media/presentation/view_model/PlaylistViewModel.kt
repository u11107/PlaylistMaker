package com.practicum.playlistmaker.media.presentation.view_model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.LocalStorageInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.models.PlaylistScreenResult
import com.practicum.playlistmaker.media.presentation.models.PlaylistScreenState
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.random.Random

open class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val localStorageInteractor: LocalStorageInteractor
) : ViewModel() {
    private val playlist: Playlist = Playlist()

    private val stateLiveData = MutableLiveData<PlaylistScreenState>()
    fun observeState(): LiveData<PlaylistScreenState> = stateLiveData

    private val resultLiveData = SingleEventLiveData<PlaylistScreenResult>()
    fun getResult(): LiveData<PlaylistScreenResult> = resultLiveData

    private val addPlaylistTrigger = SingleEventLiveData<Playlist>()
    fun getAddPlaylistTrigger(): LiveData<Playlist> = addPlaylistTrigger

    private var isClickAllowed = true
    private val onPlaylistClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickAllowed = it
        }

    init {
        setState(PlaylistScreenState.Empty)
    }

    private fun setState(state: PlaylistScreenState) {
        stateLiveData.value = state
    }

    private fun setResult(result: PlaylistScreenResult) {
        resultLiveData.postValue(result)
    }

    private fun setCurrentState() {
        when {
            !playlist.name.isNullOrEmpty() -> setState(PlaylistScreenState.Filled(playlist))
            !playlist.description.isNullOrEmpty() || !playlist.filePath.isNullOrEmpty() -> {
                setState(
                    PlaylistScreenState.NotEmpty(playlist)
                )
            }

            else -> {
                setState(PlaylistScreenState.Empty)
            }
        }
    }

    fun addPlaylist(aPlaylist: Playlist) {
        viewModelScope.launch {
            try {
                val res = playlistInteractor.addPlaylist(playlist = aPlaylist)
                playlist.id = res
                setResult(PlaylistScreenResult.Created(playlist))
            } catch (e: Exception) {
                setResult(PlaylistScreenResult.Canceled)
            }

        }
    }




    fun onAddPlaylistClick() {
        if (clickDebounce()) {
            addPlaylistTrigger.value = playlist
        }
    }

    open fun needShowDialog(): Boolean {
        return stateLiveData.value is PlaylistScreenState.Filled || stateLiveData.value is PlaylistScreenState.NotEmpty
    }

    fun onPlaylistNameChanged(text: String?) {
        playlist.name = text
        setCurrentState()
    }

    fun onPlaylistDescriptionChanged(text: String?) {
        playlist.description = text
        setCurrentState()
    }

    fun onPlaylistImageChanged(uri: Uri?) {
        if (uri == null) {
            playlist.filePath = null
        } else {
            playlist.filePath = "pl" + Random.nextInt() + ".jpg"
        }
        setCurrentState()
    }

    fun onCancelPlaylist() {
        setResult(PlaylistScreenResult.Canceled)
    }

    fun saveImageToPrivateStorage(inputStream: InputStream?) {
        val file = playlist.filePath?.let {
            File(
                localStorageInteractor.getImageDirectory(),
                it
            )
        }
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(
                Bitmap.CompressFormat.JPEG,
                COMPRESS_QUALITY_DEGREE, outputStream
            )
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onPlaylistClickDebounce(true)
        }
        return current
    }

    protected fun setPlaylistValue(value: Playlist) {
        playlist.id = value.id
        playlist.name = value.name
        playlist.description = value.description
        playlist.filePath = value.filePath
        playlist.trackList = value.trackList
        playlist.trackCount = value.trackCount
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val COMPRESS_QUALITY_DEGREE = 30
    }
}

