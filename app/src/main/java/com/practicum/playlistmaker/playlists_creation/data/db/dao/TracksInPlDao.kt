package com.practicum.playlistmaker.playlists_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.playlists_creation.data.db.entity.TrackInPlEntity

@Dao
interface TracksInPlDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPl(track: TrackInPlEntity)

    @Delete
    suspend fun deleteTrackFromPl(track: TrackInPlEntity)
}