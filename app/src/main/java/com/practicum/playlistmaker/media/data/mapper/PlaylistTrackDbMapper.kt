package com.practicum.playlistmaker.media.data.mapper

import com.practicum.playlistmaker.media.data.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistTrackDbMapper {

    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            trackId = track.trackId ?: 0,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis= track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.albumName,
            releaseYear = track.releaseYear,
            primaryGenreName = track.genreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun map(playlistTrack: PlaylistTrackEntity): Track {
        return Track(
            trackId = playlistTrack.trackId,
            trackName = playlistTrack.trackName,
            artistName = playlistTrack.artistName,
            trackTimeMillis= playlistTrack.trackTimeMillis,
            artworkUrl100 = playlistTrack.artworkUrl100,
            albumName = playlistTrack.collectionName,
            releaseYear = playlistTrack.releaseYear,
            genreName = playlistTrack.primaryGenreName,
            country = playlistTrack.country,
            previewUrl = playlistTrack.previewUrl
        )
    }
}