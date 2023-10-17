package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type

class HistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : HistoryRepository {
    override fun getSearchHistory(): ArrayList<Track> {
        return getSearchHistoryFromJson()
    }

    override suspend fun getSearchHistorySuspend(): Flow<ArrayList<Track>> = flow {
        emit(getSearchHistoryFromJson())
    }

    override suspend fun saveSearchHistory(trackList: ArrayList<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_TRACKS, createJsonFromTrackList(trackList.toTypedArray()))
            .apply()
    }

    override fun clearSearchHistory() {
        sharedPreferences.edit {
            remove(SEARCH_HISTORY_TRACKS)
        }
    }

    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val typeOfTrackList: Type = object : TypeToken<ArrayList<Track?>?>() {}.type
        return gson.fromJson(json, typeOfTrackList)
    }

    private fun createJsonFromTrackList(trackList: Array<Track>): String {
        return gson.toJson(trackList)
    }

    private fun getSearchHistoryFromJson(): ArrayList<Track> {
        val json: String = sharedPreferences.getString(SEARCH_HISTORY_TRACKS, null) ?: return ArrayList()
        return createTrackListFromJson(json)
    }

    companion object {
        const val SEARCH_HISTORY_TRACKS = "search_history_tracks"
    }
}