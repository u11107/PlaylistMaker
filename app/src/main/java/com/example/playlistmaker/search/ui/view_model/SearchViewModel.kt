package com.example.playlistmaker.search.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.util.Creator

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
) : ViewModel() {

    private val historyList = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val stateLive = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = stateLive

    private var latestSearchText: String? = null

    private val clickLiveData = MutableLiveData<Boolean>()
    fun observeClick(): LiveData<Boolean> = clickLiveData

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

        stateLive.postValue(SearchState.Loading)

        searchInteractor.searchTracks(query,
            onSuccess = { trackList ->
                stateLive.postValue(SearchState.SearchedTracks(trackList))
            },
            onError = { error ->
                stateLive.postValue(SearchState.SearchError(error))
            }
        )
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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { search(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val maxHistorySize = 10
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(Creator.provideSearchInteractor(context))
            }
        }
    }
}