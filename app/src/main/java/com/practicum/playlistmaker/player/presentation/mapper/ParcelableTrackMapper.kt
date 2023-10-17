package com.practicum.playlistmaker.player.presentation.mapper

import com.practicum.playlistmaker.player.presentation.models.ParcelableTrack
import com.practicum.playlistmaker.search.domain.models.Track

object ParcelableTrackMapper {

    fun map(track: Track): ParcelableTrack {
        return ParcelableTrack(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis ?: 0,
            artworkUrl100 = track.artworkUrl100,
            albumName = track.albumName,
            releaseYear = track.releaseYear,
            genreName = track.genreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavourite = track.isFavourite
        )
    }

    fun map(track: ParcelableTrack): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            albumName = track.albumName,
            releaseYear = track.releaseYear,
            genreName = track.genreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavourite = track.isFavourite
        )
    }
}