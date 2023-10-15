package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.model.NetworkError
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.model.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
) : ViewModel() {

    private val historyList = ArrayList<Track>()
    private var isClickAllowed = true

    private val stateLive = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = stateLive

    private var latestSearchText: String? = null


    private var searchJob: Job? = null

    init {
        historyList.addAll(searchInteractor.getHistory())
        stateLive.postValue(SearchState.SearchHistory(historyList))
    }

    override fun onCleared() {
        super.onCleared()
        searchInteractor.saveHistory(historyList)
    }

    fun search(query: String) {
        if (query.isEmpty()) return

        renderState(SearchState.Loading)

        viewModelScope.launch {
            searchInteractor.searchTracks(query)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(searchTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (searchTracks != null) {
            tracks.addAll(searchTracks)
        }

        when {
            errorMessage != null -> {
                renderState(
                    SearchState.SearchError(error = NetworkError.CONNECTION_ERROR)
                )
            }

            tracks.isEmpty() -> {
                renderState(SearchState.SearchError(error = NetworkError.EMPTY_RESULT))
            }

            else -> {
                renderState(SearchState.SearchedTracks(tracks))
            }
        }
    }

    private fun renderState(state: SearchState) {
        stateLive.postValue(state)
    }

    fun clearHistory() {
        historyList.clear()
        stateLive.postValue(SearchState.SearchHistory(historyList))
    }

    fun clearSearchText() {
        stateLive.postValue(SearchState.SearchHistory(historyList))
    }

    fun addTrackToHistory(track: Track) {
        if (historyList.contains(track)) {
            historyList.removeAt(historyList.indexOf(track))
        } else if (historyList.size == maxHistorySize) {
            historyList.removeAt(0)
        }
        historyList.add(track)
        searchInteractor.saveHistory(historyList)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }

    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            searchJob = viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

        }
        return current
    }

    companion object {
        private const val maxHistorySize = 10
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}