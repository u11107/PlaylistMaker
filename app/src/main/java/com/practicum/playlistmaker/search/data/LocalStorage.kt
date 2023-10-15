package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.model.Track

interface LocalStorage {
    fun getSearchHistory(): ArrayList<Track>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()
}