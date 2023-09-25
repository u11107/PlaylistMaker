package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.TrackResponse
import com.example.playlistmaker.search.data.network.TrackSearchRequest
import com.example.playlistmaker.search.data.storage.SearchHistoryStorage
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: SearchHistoryStorage
) : SearchRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TrackResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }


    override fun getHistory(): List<Track> {
        return storage.getHistory()
    }

    override fun saveHistory(tracks: List<Track>) {
        storage.saveHistory(tracks)
    }
}