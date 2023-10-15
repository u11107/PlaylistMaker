package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.data.api.SearchRepository
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(
    private val repository: SearchRepository,
) : SearchInteractor {

    override fun searchTracks(query: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(query).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data!!, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message!!)
                }
            }
        }
    }

    override fun getSearchHistory() = repository.getSearchHistory()

    override fun addTrackToSearchHistory(track: Track) {
        repository.addTrackToSearchHistory(track)
    }

    override fun clearSearchHistory() {
        repository.clearSearchHistory()
    }
}