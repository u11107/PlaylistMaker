package com.practicum.playlistmaker.favorites.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.favorites.data.db.entity.TrackEntity

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(track: TrackEntity)

    @Query("SELECT * FROM favorites_table")
    suspend fun getFavorites(): List<TrackEntity>

    @Query("SELECT trackId FROM favorites_table")
    suspend fun getFavoritesIds(): List<Int>

    @Delete
    suspend fun deleteFavorite(trackEntity: TrackEntity)
}