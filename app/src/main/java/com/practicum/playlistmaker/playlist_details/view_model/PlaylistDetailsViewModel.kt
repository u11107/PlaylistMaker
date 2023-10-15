package com.practicum.playlistmaker.playlist_details.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist_creation.domain.api.local_files.PlaylistsFilesInteractor
import com.practicum.playlistmaker.playlist_details.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.utils.DateUtils.getMinutesFromMillis
import com.practicum.playlistmaker.utils.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistDetailsViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistInteractor,
    private val filesInteractor: PlaylistsFilesInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private lateinit var playlist: Playlist
    private val tracks = ArrayList<Track>()

    init {
        renderScreen()
    }

    private var trackToDelete: Track? = null

    private val playlistLiveData = MutableLiveData<PlaylistDetails>()
    fun observePlaylistData(): LiveData<PlaylistDetails> = playlistLiveData

    private val tracksLiveData = MutableLiveData<TracksInPlaylistData>()
    fun observeTracksLiveDate(): LiveData<TracksInPlaylistData> = tracksLiveData

    private val toastLiveData = MutableLiveData<EmptyPlaylistToastState>()
    fun observeToastLiveData(): LiveData<EmptyPlaylistToastState> = toastLiveData

    private val menuStateLiveData = MutableLiveData<PlaylistMenuState>()
    fun observePlaylistMenuState(): LiveData<PlaylistMenuState> = menuStateLiveData

    private fun renderScreen() {
        viewModelScope.launch {
            playlist = getPlaylistById()
            tracks.addAll(initTrackList())
            renderPlaylistData()
            renderTracksData()
        }
    }

    private suspend fun getPlaylistById(): Playlist = withContext(Dispatchers.IO) {
        playlistInteractor.getPlaylist(playlistId)
    }

    private suspend fun initTrackList(): List<Track> = withContext(Dispatchers.IO) {
        playlistInteractor.getTracksInPlaylist(playlist)
    }

    private fun renderPlaylistData() {
        playlistLiveData.value = PlaylistDetails(
            playlist.coverUri,
            playlist.name,
            playlist.description,
        )
    }

    private fun getDuration(): Int {
        var durationInMillis = 0
        for (track in tracks) {
            durationInMillis += track.duration
        }
        return getMinutesFromMillis(durationInMillis)
    }

    private fun renderTracksData() {
        tracksLiveData.value = TracksInPlaylistData(getDuration(), playlist.numberOfTracks, tracks)
    }

    fun onTrackLongClicked(track: Track) {
        trackToDelete = track
    }

    fun onTrackDeleteConfirmed() {
        viewModelScope.launch {
            deleteTrackFromPlaylist()
            tracks.remove(trackToDelete)
            playlist = playlist.copy(numberOfTracks = playlist.numberOfTracks - 1)
            trackToDelete = null
            renderTracksData()
        }
    }

    private fun deleteTrackFromPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            if (trackToDelete != null) playlistInteractor.deleteTrackFromPlaylist(
                trackToDelete!!.trackId,
                playlistId
            )
        }
    }

    fun onShareClicked(numberOfTracks: String) {
        if (tracks.isEmpty()) toastLiveData.value = EmptyPlaylistToastState.SHOW
        else sharingInteractor.shareString(
            TextUtils.getSharedTracksString(
                playlist,
                tracks,
                numberOfTracks
            )
        )
    }

    fun onMenuClicked() {
        menuStateLiveData.value = PlaylistMenuState.SHOW
    }

    fun toastWasShown() {
        toastLiveData.value = EmptyPlaylistToastState.NONE
    }

    suspend fun onPlaylistDeleteConfirmed() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(playlistId)
            filesInteractor.deleteFromPrivateStorage(playlist.coverUri)
        }
    }

    fun menuWasShown() {
        menuStateLiveData.value = PlaylistMenuState.NONE
    }

    fun onResume() {
        viewModelScope.launch {
            playlist = getPlaylistById()
            renderPlaylistData()
        }
    }
}