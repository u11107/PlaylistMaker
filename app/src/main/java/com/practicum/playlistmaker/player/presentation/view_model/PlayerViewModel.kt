package com.practicum.playlistmaker.player.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.FavouritesInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.presentation.models.PlayerScreenMode
import com.practicum.playlistmaker.player.presentation.models.PlayerScreenState
import com.practicum.playlistmaker.player.presentation.models.TrackAddProcessStatus
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleEventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor,
    private val historyInteractor: HistoryInteractor,
    private val playlistInteractor: PlaylistInteractor,
) :
    ViewModel() {

    private val modeLiveData = MutableLiveData<PlayerScreenMode>()
    fun observeMode(): LiveData<PlayerScreenMode> = modeLiveData

    private val stateLiveData = MutableLiveData<PlayerScreenState>()
    fun observeState(): LiveData<PlayerScreenState> = stateLiveData

    private val favouriteLiveData = MutableLiveData<Boolean>()
    fun observeFavourite(): LiveData<Boolean> = favouriteLiveData

    private val trackAddProcessStatus = SingleEventLiveData<TrackAddProcessStatus>()
    fun getAddProcessStatus(): LiveData<TrackAddProcessStatus> = trackAddProcessStatus

    private var currentTime: String? = null
    private var timerJob: Job? = null
    private var playlists: List<Playlist> = listOf()

    init {
        loadPlayer()
        viewModelScope.launch {
            track.isFavourite = favouritesInteractor.getFavouriteState(track.trackId ?: 0)
            setFavourite(track.isFavourite)
        }

        viewModelScope.launch {
            playlistInteractor
                .getFlowPlaylists()
                .collect {
                    playlists = it
                }
        }
        setMode(PlayerScreenMode.Player)
    }

    private fun loadPlayer() {
        setState(PlayerScreenState.Default(track))
        playerInteractor.preparePlayer(track.previewUrl, {
            setState(PlayerScreenState.Prepared)
        }, {
            setState(PlayerScreenState.Paused(DEFAULT_TIME))
        }, {

        })
    }

    private fun setState(state: PlayerScreenState) {
        stateLiveData.value = state
    }

    private fun setFavourite(isFavourite: Boolean) {
        favouriteLiveData.value = isFavourite
    }

    private fun setMode(mode: PlayerScreenMode) {
        modeLiveData.value = mode
    }

    private fun setAddProcessStatus(status: TrackAddProcessStatus) {
        trackAddProcessStatus.value = status
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (stateLiveData.value is PlayerScreenState.Playing || stateLiveData.value is PlayerScreenState.Progress) {
                currentTime = getCurrentTime()
                setState(PlayerScreenState.Progress(getCurrentTime()))
                delay(TIME_DEBOUNCE_DELAY_MILLIS)
            }
        }
    }

    private fun onStartPlayer() {
        setState(PlayerScreenState.Playing())
        startTimer()
    }

    private fun onPausePlayer() {
        setState(PlayerScreenState.Paused(currentTime))
        timerJob?.cancel()
    }

    private fun getCurrentTime(): String? = playerInteractor.getCurrentPosition(DEFAULT_TIME)

    fun playBackControl() {
        playerInteractor.playbackControl({ onStartPlayer() }, { onPausePlayer() })
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer { onPausePlayer() }
    }

    private fun saveFavouriteTrack() {
        viewModelScope.launch {
            favouritesInteractor.saveFavouriteTrack(track)
        }
    }

    private fun deleteFavouriteTrack() {
        viewModelScope.launch {
            favouritesInteractor.deleteFavouriteTrack(track.trackId!!)
        }
    }

    fun onLikeTrackClick() {
        track.isFavourite = !track.isFavourite
        setFavourite(track.isFavourite)
        if (track.isFavourite) {
            saveFavouriteTrack()
        } else {
            deleteFavouriteTrack()
        }
        viewModelScope.launch{
            historyInteractor.addTrackToSearchHistory(track)
        }
    }

    fun onNewPlaylistClick() {
        setMode(PlayerScreenMode.NewPlaylist)
    }

    fun onCancelBottomSheet() {
        setMode(PlayerScreenMode.Player)
    }

    fun onPlayerAddTrackClick() {
        setMode(PlayerScreenMode.BottomSheet(playlists))
    }

    fun addTrackToPlaylist(playlistId: Long, playlistName: String?) {
        viewModelScope.launch {
            try {
                playlistInteractor.addTrackToPlaylist(track, playlistId)
                setAddProcessStatus(TrackAddProcessStatus.Added(playlistName))
            } catch (e: Exception) {
                setAddProcessStatus(TrackAddProcessStatus.Error(playlistName))
            }
        }
        setMode(PlayerScreenMode.Player)
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        if (playlist.trackList.indexOf(track.trackId) == -1) {
            addTrackToPlaylist(playlist.id, playlist.name)
        } else {
            setAddProcessStatus(TrackAddProcessStatus.Exist(playlist.name))
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    companion object {
        private const val DEFAULT_TIME = "00:00"
        private const val TIME_DEBOUNCE_DELAY_MILLIS = 300L
    }
}