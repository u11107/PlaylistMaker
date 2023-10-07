package com.practicum.playlistmaker.playlists_creation.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_tracks_cross_ref_table", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTracksCrossRef(
    val trackId: Int,
    val playlistId: Int
)