package com.practicum.playlistmaker.media.presentation.mapper

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.models.ParcelablePlaylist

object ParcelablePlaylistMapper {

    fun map(playlist: Playlist): ParcelablePlaylist {
        return ParcelablePlaylist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            filePath = playlist.filePath,
            trackList = playlist.trackList,
            trackCount = playlist.trackCount
        )
    }

    fun map(parcelablePlaylist: ParcelablePlaylist): Playlist {
        return Playlist(
            id = parcelablePlaylist.id,
            name = parcelablePlaylist.name,
            description = parcelablePlaylist.description,
            filePath = parcelablePlaylist.filePath,
            trackList = parcelablePlaylist.trackList,
            trackCount = parcelablePlaylist.trackCount
        )
    }
}