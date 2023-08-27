package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.domain.model.Track

data class TrackResponse(
    val results: List<Track>
) : Response()
