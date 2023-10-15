package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val country: String,
    val releaseDate: String?,
    @SerializedName("trackTimeMillis") val duration: Int?,
    @SerializedName("artworkUrl100") val artworkUri: String,
    @SerializedName("primaryGenreName") val genre: String,
    @SerializedName("collectionName") val album: String,
    val previewUrl: String?
)