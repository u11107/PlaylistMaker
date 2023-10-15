package com.practicum.playlistmaker.favorites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favorites.data.db.dao.FavoritesDao
import com.practicum.playlistmaker.favorites.data.db.entity.FavoritesTrackEntity
import com.practicum.playlistmaker.playlist_creation.data.db.dao.PlaylistTracksCrossRefDao
import com.practicum.playlistmaker.playlist_creation.data.db.dao.PlaylistsDao
import com.practicum.playlistmaker.playlist_creation.data.db.dao.TracksInPlaylistDao
import com.practicum.playlistmaker.playlist_creation.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.playlist_creation.data.db.entity.PlaylistTracksCrossRef
import com.practicum.playlistmaker.playlist_creation.data.db.entity.TrackInPlEntity

@Database(
    version = 1,
    entities = [
        FavoritesTrackEntity::class,
        PlaylistEntity::class,
        TrackInPlEntity::class,
        PlaylistTracksCrossRef::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun playlistsDao(): PlaylistsDao
    abstract fun tracksInPlDao(): TracksInPlaylistDao
    abstract fun playlistsTracksCrossRefDao(): PlaylistTracksCrossRefDao
}