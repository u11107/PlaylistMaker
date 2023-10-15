package com.practicum.playlistmaker.search.domain.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val country: String,
    val releaseDate: String,
    val releaseYear: String,
    val duration: Int,
    val lowResArtworkUri: String,
    val midResArtworkUri: String,
    val highResArtworkUri: String,
    val genre: String,
    val album: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false
) : Parcelable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
