package com.practicum.playlistmaker.playlist_creation.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_pl_table")
data class TrackInPlEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val country: String,
    val releaseDate: String,
    val releaseYear: String,
    val duration: Int,
    val lowResArtworkUri: String,
    val midResArtworkUri: String,
    val highResArtworkUri: String,
    val genre: String,
    val album: String,
    val previewUrl: String?,
)