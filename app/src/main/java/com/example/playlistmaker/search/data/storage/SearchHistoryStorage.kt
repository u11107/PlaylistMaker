package com.example.playlistmaker.search.data.storage

import com.example.playlistmaker.search.domain.model.Track

interface SearchHistoryStorage {
    fun saveHistory(tracks: List<Track>)
    fun getHistory(): List<Track>
    fun clearHistory()
}