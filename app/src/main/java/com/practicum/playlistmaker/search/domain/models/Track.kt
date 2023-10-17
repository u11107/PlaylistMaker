package com.practicum.playlistmaker.search.domain.models

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long? = 0,
    val artworkUrl100: String?,
    val albumName: String?,
    val releaseYear: Int?,
    val genreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavourite: Boolean = false,
) {
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun getCoverArtwork60() = artworkUrl100?.replaceAfterLast('/', "60x60bb.jpg")

    fun getTrackTime() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis ?: 0).orEmpty()
}