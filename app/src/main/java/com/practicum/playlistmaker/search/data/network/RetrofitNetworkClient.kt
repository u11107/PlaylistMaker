package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesAPIService: ITunesApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            withContext(Dispatchers.IO) {
                try {
                    val resp = iTunesAPIService.search(dto.expression)
                    resp.apply { resultCode = 200 }
                } catch (e: Exception) {
                    Response().apply { resultCode = 400 }
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}