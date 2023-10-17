package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.media.data.AppDatabase
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : TrackRepository {


    override fun search(
        expression: String
    ): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            val favouriteTracks = appDatabase.trackDao().getTrackIds()
            val arrayList = ArrayList<Track>((response as TrackSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    getReleaseYear(it.releaseDate),
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl,
                    isFavouriteTrack(it.trackId, favouriteTracks)
                )
            })
            emit(Resource.Success(arrayList))
        } else {
            emit(Resource.Error("Ошибка сервера"))
        }
    }

    private fun getReleaseYear(date: Date?): Int? {
        return if (date != null) {
            (SimpleDateFormat("yyyy", Locale.getDefault()).format(date).orEmpty()).toInt()
        } else {
            null
        }
    }

    private fun isFavouriteTrack(trackId: Long, favouriteTracks: List<Long>): Boolean {
        return favouriteTracks.indexOf(trackId) > -1
    }
}