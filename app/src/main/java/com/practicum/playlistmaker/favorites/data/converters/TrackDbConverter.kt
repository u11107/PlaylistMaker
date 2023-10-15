package com.practicum.playlistmaker.favorites.data.converters

import com.practicum.playlistmaker.favorites.data.db.entity.TrackEntity
import com.practicum.playlistmaker.playlists_creation.data.db.entity.TrackInPlEntity
import com.practicum.playlistmaker.search.domain.model.Track

class TrackDbConverter {

    fun map(track: TrackEntity): Track =
        Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.country,
            track.releaseDate,
            track.releaseYear,
            track.duration,
            track.lowResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
            true
        )

    fun map(track: Track, addingTime: Long): TrackEntity =
        TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.country,
            track.releaseDate,
            track.releaseYear,
            track.duration,
            track.lowResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
            addingTime
        )

    fun map(track: TrackInPlEntity): Track =
        Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.country,
            track.releaseDate,
            track.releaseYear,
            track.duration,
            track.lowResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
            true
        )

    fun map(track: Track): TrackInPlEntity =
        TrackInPlEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.country,
            track.releaseDate,
            track.releaseYear,
            track.duration,
            track.lowResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
        )
}