package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun getSearchHistory() : ArrayList<Track>

    suspend fun addTrackToSearchHistory(track: Track)

    fun clearSearchHistory()
}