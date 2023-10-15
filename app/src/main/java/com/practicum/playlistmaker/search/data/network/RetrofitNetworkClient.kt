package com.practicum.playlistmaker.search.data.network

import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class RetrofitNetworkClient(
    private val itunesService: ItunesService,
    private val capabilities: NetworkCapabilities?
) : NetworkClient, KoinComponent {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_CONNECTIVITY_ERROR }
        }
        if (dto !is SearchRequest) {
            return Response().apply { resultCode = BAD_REQUEST_ERROR }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = itunesService.search(dto.query)
                response.apply { resultCode = SUCCESSFUL_REQUEST }
            } catch (e: Throwable) {
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
        const val BAD_REQUEST_ERROR = 400
        const val SERVER_ERROR = 500
        const val SUCCESSFUL_REQUEST = 200
        const val NO_CONNECTIVITY_ERROR = -1
    }
}