package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.model.NetworkError

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    override fun searchTracks(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        repository.searchTracks(query, onSuccess, onError)
    }

    override fun getHistory(): List<Track> {
       return repository.getHistory()
    }

    override fun saveHistory(tracks: List<Track>) {
        repository.saveHistory(tracks)
    }
}