package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.util.STATUS_CODE_BAD_REQUEST
import com.example.playlistmaker.util.STATUS_CODE_NO_NETWORK_CONNECTION
import com.example.playlistmaker.util.STATUS_CODE_SERVER_ERROR
import com.example.playlistmaker.util.STATUS_CODE_SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val api: TrackApi, private val context: Context):NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = STATUS_CODE_NO_NETWORK_CONNECTION }
        }

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = STATUS_CODE_BAD_REQUEST }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = api.search(dto.expression)
                response.apply { resultCode = STATUS_CODE_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}