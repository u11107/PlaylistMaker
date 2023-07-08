package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.model.NetworkError
import com.example.playlistmaker.search.domain.model.Track

interface SearchRepository {
    fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit)
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}