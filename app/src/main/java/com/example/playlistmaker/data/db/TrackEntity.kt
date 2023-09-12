package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.data.db.TrackEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
    val favouriteAddedTimestamp: Long
) {
    companion object {
        const val TABLE_NAME = "track_table"
    }
}
