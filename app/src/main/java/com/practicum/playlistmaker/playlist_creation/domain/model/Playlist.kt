package com.practicum.playlistmaker.playlist_creation.domain.model

import com.google.gson.Gson

data class Playlist(
    val playlistId: Int = 0,
    val name: String = "",
    val description: String = "",
    val coverUri: String = "",
    val tracks: ArrayList<Int> = ArrayList(),
    val numberOfTracks: Int = 0
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}