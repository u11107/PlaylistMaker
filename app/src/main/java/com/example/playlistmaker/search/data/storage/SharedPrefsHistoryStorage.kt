package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson

class SharedPrefsHistoryStorage(private val sharedPrefs: SharedPreferences): SearchHistoryStorage {

    override fun saveHistory(tracks: List<Track>) {
        val tracksJson = Gson().toJson(tracks)
        sharedPrefs.edit().putString(TRACK_SEARCH_HISTORY, tracksJson).apply()
    }

    override fun getHistory(): List<Track> {
        val tracksJson = sharedPrefs.getString(TRACK_SEARCH_HISTORY, null) ?: return emptyList()
        return Gson().fromJson(tracksJson, Array<Track>::class.java).toList()
    }

    override fun clearHistory() {
        sharedPrefs.edit().clear().apply()
    }

    companion object {
        const val TRACK_SEARCH_HISTORY = "TRACK_SEARCH_HISTORY"
    }
}