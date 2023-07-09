package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.domain.model.NetworkError
import com.example.playlistmaker.search.domain.model.Track

interface NetworkClient {
    fun doRequest(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit)
}