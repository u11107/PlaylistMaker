package com.practicum.playlistmaker.playlist_creation.data.converters

import com.practicum.playlistmaker.playlist_creation.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity =
        PlaylistEntity(
            playlistId = playlist.playlistId,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            numberOfTracks = playlist.numberOfTracks
        )

    fun map(playlist: PlaylistEntity): Playlist =
        Playlist(
            playlistId = playlist.playlistId,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            tracks = ArrayList(),
            numberOfTracks = playlist.numberOfTracks
        )
}