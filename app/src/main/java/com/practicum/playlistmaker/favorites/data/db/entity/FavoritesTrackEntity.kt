package com.practicum.playlistmaker.favorites.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
class FavoritesTrackEntity(
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
    val addingTime: Long,
) {
}