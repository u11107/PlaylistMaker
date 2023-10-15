package com.practicum.playlistmaker.utils

object TextUtils {

    fun getHighResArtwork(lowResArtworkUri: String) =
        lowResArtworkUri.replaceAfterLast('/', "512x512bb.jpg")

    fun getCoverImageName(coverName: String) = "$coverName.jpg"
    fun numberOfTracksString(numberOfTracks: Int): String =
        when (numberOfTracks % 100) {
            in (5..20) -> "$numberOfTracks треков"
            else -> when (numberOfTracks % 10) {
                1 -> "$numberOfTracks трек"
                in (2..4) -> "$numberOfTracks трека"
                else -> "$numberOfTracks треков"
            }
        }
}