package com.example.playlistmaker.data.db.track

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(track: TrackEntity)

    @Query("DELETE FROM ${TrackEntity.TABLE_NAME} WHERE trackId = :trackId")
    suspend fun deleteFromFavorites(trackId: Int)

    @Query("SELECT * FROM ${TrackEntity.TABLE_NAME} ORDER BY favouriteAddedTimestamp DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM ${TrackEntity.TABLE_NAME}  WHERE trackId = :trackId)")
    suspend fun isFavoriteTrack(trackId: Int): Boolean
}