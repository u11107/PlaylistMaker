package com.practicum.playlistmaker.search.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.models.SearchScreenState
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = stateLiveData

    private val showPlayerTrigger = SingleEventLiveData<Track>()
    fun getShowPlayerTrigger(): LiveData<Track> = showPlayerTrigger

    private var lastSearchText: String? = null
    private var currentSearchText: String? = null

    private var isClickAllowed = true

    private val onTrackSearchDebounce =
        debounce<String?>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            if (it?.isEmpty() == false)
                search(it)
        }

    private val onTrackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickAllowed = it
        }

    private fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        this.lastSearchText = changedText
        onTrackSearchDebounce(changedText)
    }

    private fun setState(state: SearchScreenState) {
        stateLiveData.postValue(state)
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            setState(SearchScreenState.List(ArrayList()))
            return
        }
        setState(SearchScreenState.Progress)

        viewModelScope.launch {
            trackInteractor
                .search(searchText)
                .collect {
                    val tracks = ArrayList<Track>()
                    if (it.first != null) {
                        tracks.addAll(it.first!!)
                    }

                    when {
                        it.second != null -> {
                            setState(SearchScreenState.Error)
                        }

                        tracks.isEmpty() -> {
                            setState(SearchScreenState.Empty)
                        }

                        else -> {
                            setState(SearchScreenState.List(tracks = tracks))
                        }
                    }
                }
        }
    }


    fun onRetryButtonClick() {
        search(currentSearchText ?: "")
    }

    fun onEditTextChanged(hasFocus: Boolean, text: String?) {
        currentSearchText = text
        val tracks = getHistoryTrackList()
        if (hasFocus && currentSearchText?.isEmpty() == true && tracks.size > 0) {
            onTrackSearchDebounce(currentSearchText)
            setState(SearchScreenState.History(tracks))
        } else {
            searchDebounce(currentSearchText ?: "")
            setState(
                SearchScreenState.List(
                    if (stateLiveData.value is SearchScreenState.List) {
                        (stateLiveData.value as SearchScreenState.List).tracks
                    } else {
                        ArrayList()
                    }
                )
            )
        }
    }

    fun onEditFocusChange(hasFocus: Boolean) {
        val tracks = getHistoryTrackList()
        if (hasFocus && currentSearchText.isNullOrEmpty() && tracks.size > 0) {
            onTrackSearchDebounce(currentSearchText)
            setState(SearchScreenState.History(tracks))
        } else {
            setState(
                SearchScreenState.List(
                    if (stateLiveData.value is SearchScreenState.List) {
                        (stateLiveData.value as SearchScreenState.List).tracks
                    } else {
                        ArrayList()
                    }
                )
            )
        }
    }

    fun onClearSearchHistoryButtonClick() {
        clearHistoryTrackList()
        setState(SearchScreenState.List(ArrayList()))
    }

    fun onEditorAction() {
        search(currentSearchText ?: "")
    }

    fun showPlayer(track: Track) {
        if (clickDebounce()) {
            showPlayerTrigger.value = track
        }
    }

    fun addTrackToSearchHistory(track: Track) {
        viewModelScope.launch {
            historyInteractor.addTrackToSearchHistory(track)
        }
    }

    private fun getHistoryTrackList(): ArrayList<Track> {
        return historyInteractor.getSearchHistory()
    }

    private fun clearHistoryTrackList() {
        historyInteractor.clearSearchHistory()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}