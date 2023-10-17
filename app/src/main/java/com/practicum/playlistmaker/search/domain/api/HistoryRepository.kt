package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getSearchHistory() : ArrayList<Track>

    suspend fun getSearchHistorySuspend() : Flow<ArrayList<Track>>

    suspend fun saveSearchHistory(trackList: ArrayList<Track>)

    fun clearSearchHistory()
}