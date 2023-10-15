package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.NetworkError
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import javax.xml.xpath.XPathExpression

interface SearchInteractor {
    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>>
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}