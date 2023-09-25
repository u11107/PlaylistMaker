package com.example.playlistmaker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.playlist.PlaylistDao
import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.data.db.track.TrackDao
import com.example.playlistmaker.data.db.track.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class, PlaylistEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun TrackDao(): TrackDao
    abstract fun PlaylistDao(): PlaylistDao
}