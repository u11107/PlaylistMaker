package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.storage.SearchHistoryStorage
import com.example.playlistmaker.search.domain.model.NetworkError

class SearchRepositoryImpl(
    private val networkClient: NetworkClient, private val
                           searchHistoryStorage: SearchHistoryStorage): SearchRepository {

    override fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit) {
        networkClient.doRequest(query, onSuccess, onError)
    }

    override fun getHistory(): List<Track> {
        return searchHistoryStorage.getHistory()
    }

    override fun saveHistory(tracks: List<Track>) {
        searchHistoryStorage.saveHistory(tracks)
    }
}