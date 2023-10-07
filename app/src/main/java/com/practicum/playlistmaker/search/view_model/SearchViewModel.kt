package com.practicum.playlistmaker.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.ResourceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val resourceProvider: ResourceProvider,
    private val interactor: SearchInteractor,
) : ViewModel() {

    //todo save search state when rotate screen

    private var lastUnsuccessfulSearch: String = ""
    private val stateLiveData = MutableLiveData<SearchState>()
    private val clearTextState = MutableLiveData<ClearTextState>(ClearTextState.None)
    private val searchTextLiveData = MutableLiveData<String>()
    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    fun observeState(): LiveData<SearchState> = stateLiveData
    fun observeClearTextState(): LiveData<ClearTextState> = clearTextState
    fun observeSearchText(): LiveData<String> = searchTextLiveData

    fun textCleared() {
        clearTextState.value = ClearTextState.None
    }

    fun onClearTextPressed() {
        clearTextState.value = ClearTextState.ClearText
        showSearchHistory()
    }

    fun onTextChanged(changedText: String) {
        if (changedText == "") {
            searchJob?.cancel()
            showSearchHistory()
        } else {
            searchDebounce(changedText)
        }
    }

    fun onClearSearchHistoryPressed() {
        interactor.clearSearchHistory()
        renderState(SearchState.EmptyScreen)
    }

    fun onRefreshSearchButtonPressed() {
        searchRequest(lastUnsuccessfulSearch)
    }

    fun onTrackPressed(track: Track) {
        interactor.addTrackToSearchHistory(track)
    }

    fun onFocusChanged(hasFocus: Boolean, searchText: String) {
        if (hasFocus && searchText.isEmpty()) {
            showSearchHistory()
        }
    }

    fun onResume() {
        if (stateLiveData.value is SearchState.HistoryContent) showSearchHistory()
        else if (stateLiveData.value is SearchState.SearchContent) searchRequest(latestSearchText!!)
    }

    fun onDestroy() {
        searchTextLiveData.postValue(latestSearchText ?: "")
    }

    private fun showSearchHistory() {
        viewModelScope.launch {
            interactor.getSearchHistory().collect {
                if (it.isNotEmpty()) renderState(SearchState.HistoryContent(it))
                else renderState(SearchState.EmptyScreen)
            }
        }
    }

    private fun searchDebounce(searchText: String) {
        if (latestSearchText == searchText) {
            return
        }
        latestSearchText = searchText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(searchText)
        }
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                interactor
                    .searchTracks(searchText)
                    .collect { pair ->
                        val tracks = ArrayList<Track>()
                        if (pair.first != null) {
                            tracks.addAll(pair.first!!)
                            if (tracks.isNotEmpty()) {
                                renderState(SearchState.SearchContent(tracks))
                            } else {
                                renderState(
                                    SearchState.EmptySearch(
                                        resourceProvider.getString(
                                            R.string.nothing_is_found
                                        )
                                    )
                                )
                            }
                        }
                        if (pair.second != null) {
                            lastUnsuccessfulSearch = searchText
                            renderState(SearchState.Error(pair.second!!))
                        }
                    }
            }
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }
}