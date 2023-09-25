package com.example.playlistmaker.data.db.convertor

import androidx.core.net.toUri
import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.data.db.track.TrackEntity
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import java.util.Calendar

class DbConverter {

    fun mapFromTrackEntityToTrack(from: TrackEntity): Track {
        return Track(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
        )
    }

    fun mapFromTrackToTrackEntity(from: Track): TrackEntity {
        return TrackEntity(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
            Calendar.getInstance().timeInMillis
        )
    }

    fun mapFromPlaylistEntityToPlaylist(from: PlaylistEntity): Playlist {
        return Playlist(
            id = from.id,
            title = from.title,
            description = from.description,
            imageUri = from.imageUri.toUri(),
            trackList = from.trackList,
            size = from.size
        )
    }

    fun mapFromPlaylistToPlaylistEntity(from: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = from.id,
            title = from.title,
            description = from.description,
            imageUri = from.imageUri.toString(),
            trackList = from.trackList,
            size = from.size
        )
    }
}