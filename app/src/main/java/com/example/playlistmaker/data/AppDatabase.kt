package com.example.playlistmaker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.TrackDao
import com.example.playlistmaker.data.db.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun TrackDao(): TrackDao
}