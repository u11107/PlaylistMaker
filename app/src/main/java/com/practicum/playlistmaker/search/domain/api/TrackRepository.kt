package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun search(expression: String): Flow<Resource<ArrayList<Track>>>
}