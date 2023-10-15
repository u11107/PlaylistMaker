package com.practicum.playlistmaker.favorites.data.converters

import com.practicum.playlistmaker.favorites.data.db.entity.FavoritesTrackEntity
import com.practicum.playlistmaker.playlist_creation.data.db.entity.TrackInPlEntity
import com.practicum.playlistmaker.search.domain.model.Track

class TrackDbConverter {

    fun map(track: FavoritesTrackEntity): Track =
        Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.country,
            track.releaseDate,
            track.releaseYear,
            track.duration,
            track.lowResArtworkUri,
            track.midResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
            true
        )

    fun map(track: Track, addingTime: Long): FavoritesTrackEntity =
        FavoritesTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.country,
            track.releaseDate,
            track.releaseYear,
            track.duration,
            track.lowResArtworkUri,
            track.midResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
            addingTime
        )

    fun map(track: TrackInPlEntity, isFavorite: Boolean): Track =
        Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.country,
            track.releaseDate,
            track.releaseYear,
            track.duration,
            track.lowResArtworkUri,
            track.midResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
            isFavorite
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
            track.midResArtworkUri,
            track.highResArtworkUri,
            track.genre,
            track.album,
            track.previewUrl,
        )
}