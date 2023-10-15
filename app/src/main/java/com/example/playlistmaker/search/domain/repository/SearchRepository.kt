package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}