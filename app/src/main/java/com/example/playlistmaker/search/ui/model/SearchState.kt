package com.example.playlistmaker.search.ui.model

import com.example.playlistmaker.search.domain.model.NetworkError
import com.example.playlistmaker.search.domain.model.Track

sealed class SearchState {
    class SearchHistory(val tracks: List<Track>): SearchState()
    class SearchedTracks(val tracks: List<Track>): SearchState()
    class SearchError(val error: NetworkError) : SearchState()
    object Loading : SearchState()
}