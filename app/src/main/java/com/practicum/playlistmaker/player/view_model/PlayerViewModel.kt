package com.practicum.playlistmaker.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorites.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.playlists_creation.domain.api.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.playlists_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.DateUtils.formatTime
import com.practicum.playlistmaker.utils.ResourceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val resourceProvider: ResourceProvider,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsDbInteractor: PlaylistsDbInteractor
) : ViewModel() {

    private val isFavoriteLiveData = MutableLiveData<Boolean>()

    init {
        isFavoriteLiveData.value = track.isFavorite
    }

    private val stateLiveData = MutableLiveData<PlayerState>()
    private val toastLiveData = MutableLiveData<ToastState>()
    private val playlistsLiveData = MutableLiveData<PlaylistsInPlayerState>()
    private var timerJob: Job? = null

    public override fun onCleared() {
        playerInteractor.releasePlayer()
        timerJob?.cancel()
        stateLiveData.value = PlayerState.DefaultState()
    }

    fun preparePlayer() {
        if (track.previewUrl != null) {
            playerInteractor.preparePlayer(
                trackUri = track.previewUrl,
                onPrepared = {
                    renderState(PlayerState.PreparedState())
                },
                onCompletion = {
                    timerJob?.cancel()
                    renderState(PlayerState.PreparedState())
                }
            )
        } else {
            showToast(resourceProvider.getString(R.string.no_preview_url))
            renderState(PlayerState.PreparedState())
        }
    }

    fun observeState(): LiveData<PlayerState> = stateLiveData
    fun observeToastState(): LiveData<ToastState> = toastLiveData
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData
    fun observePlaylists(): LiveData<PlaylistsInPlayerState> = playlistsLiveData

    fun onPlayPressed() {
        if (track.previewUrl == null) {
            showToast(resourceProvider.getString(R.string.no_preview_url))
        } else {
            playbackControl()
        }
    }

    fun toastWasShown() {
        toastLiveData.value = ToastState.None
    }

    fun onPause() {
        pausePlayer()
    }

    fun onFavoriteClicked() {
        isFavoriteLiveData.value = !track.isFavorite
        viewModelScope.launch {
            if (track.isFavorite) {
                favoritesInteractor.deleteFavorite(track)
                track.isFavorite = false
            } else {
                favoritesInteractor.addFavorite(track)
                track.isFavorite = true
            }
        }
    }

    fun addToPlaylistClicked() {
        viewModelScope.launch {
            playlistsDbInteractor.getPlaylists().collect {
                playlistsLiveData.postValue(PlaylistsInPlayerState.DisplayPlaylists(it))
            }
        }
    }

    fun onPlaylistClicked(playlist: Playlist) {
        if (playlist.tracks.contains(track.trackId)) {
            showToast("${resourceProvider.getString(R.string.track_is_in_pl_already)} ${playlist.name}")
        } else {
            viewModelScope.launch {
                playlist.tracks.add(track.trackId)
                val updatedPlaylist = playlist.copy(numberOfTracks = playlist.numberOfTracks + 1)
                playlistsDbInteractor.addTrackToPl(track, updatedPlaylist)
            }
            showToast("${resourceProvider.getString(R.string.track_added_to_pl)} ${playlist.name}")
        }
        playlistsLiveData.postValue(PlaylistsInPlayerState.HidePlaylists)
    }

    fun onResume() {
        if (playlistsLiveData.value is PlaylistsInPlayerState.DisplayPlaylists) addToPlaylistClicked()
    }

    private fun renderState(playerState: PlayerState) {
        stateLiveData.postValue(playerState)
    }

    private fun showToast(message: String) {
        toastLiveData.value =
            ToastState.Show(message)
    }

    private fun playbackControl() {
        when (stateLiveData.value) {
            is PlayerState.PlayingState -> pausePlayer()
            is PlayerState.PauseState, is PlayerState.PreparedState -> startPlayer()
            else -> {}
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerState.PlayingState(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        timerJob?.cancel()
        renderState(PlayerState.PauseState(getCurrentPlayerPosition()))
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(PLAYBACK_TIME_REFRESH)
                stateLiveData.postValue(PlayerState.PlayingState(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return formatTime(playerInteractor.getPlayerPosition())
    }

    companion object {
        private const val PLAYBACK_TIME_REFRESH = 200L
    }
}