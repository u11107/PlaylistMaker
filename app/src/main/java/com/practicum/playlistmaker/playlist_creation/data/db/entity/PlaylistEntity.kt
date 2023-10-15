package com.practicum.playlistmaker.playlist_creation.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val name: String,
    val description: String,
    val coverUri: String,
    val numberOfTracks: Int
)
