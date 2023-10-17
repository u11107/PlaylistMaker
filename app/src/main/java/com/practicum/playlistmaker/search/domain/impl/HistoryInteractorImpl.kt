package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository): HistoryInteractor {
    override fun getSearchHistory(): ArrayList<Track> {
        return historyRepository.getSearchHistory()
    }

    override suspend fun addTrackToSearchHistory(track: Track) {
        historyRepository.getSearchHistorySuspend().collect { result ->
            val trackIds = ArrayList<Long>(result.map { it.trackId ?: 0 })
            val trackInd = trackIds.indexOf(track.trackId)
            if (trackInd > -1) {
                result.removeAt(trackInd)
            }
            if (result.size > 9) {
                result.removeAt(0)
            }
            result.add(track)
            historyRepository.saveSearchHistory(result)
        }
    }

    override fun clearSearchHistory() {
        historyRepository.clearSearchHistory()
    }
}