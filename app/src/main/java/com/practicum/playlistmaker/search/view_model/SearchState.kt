package com.practicum.playlistmaker.search.view_model

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface SearchState {

    object Loading : SearchState
    data class SearchContent(val tracks: List<Track>) : SearchState
    data class HistoryContent(val tracks: List<Track>) : SearchState
    data class Error(val errorMessage: String) : SearchState
    data class EmptySearch(val emptySearchMessage: String) : SearchState
    object EmptyScreen : SearchState

}