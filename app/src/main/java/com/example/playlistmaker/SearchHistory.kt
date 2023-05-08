package com.example.playlistmaker

import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SearchHistory {

    const val HISTORY = "history"
    const val SIZE = 10

    private fun createJsonFromTrackList(trackList: ArrayList<Track>) {
        val json = Gson().toJson(trackList)
        App.sharedMemory.edit()
            .putString(HISTORY, json)
            .apply()
    }


    fun fillInList(): ArrayList<Track> {
        var historyList = ArrayList<Track>()
        val getShare = App.sharedMemory.getString(HISTORY, null)
        if (!getShare.isNullOrEmpty()) {
            val sType = object : TypeToken<ArrayList<Track>>() {}.type
            historyList = Gson().fromJson(getShare, sType)
        }
        return historyList
    }

    fun addTrack(track: Track) {
        val historyTrackList = fillInList()
        historyTrackList.remove(track)
        if (historyTrackList.size >= SIZE) {
            historyTrackList.removeAt(historyTrackList.size - 1)
        }
        historyTrackList.add(0, track)
        createJsonFromTrackList(historyTrackList)
    }

    fun clear() {
        createJsonFromTrackList(ArrayList<Track>())
    }
}