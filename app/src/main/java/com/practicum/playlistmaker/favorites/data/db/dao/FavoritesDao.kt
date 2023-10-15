package com.practicum.playlistmaker.favorites.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.favorites.data.db.entity.FavoritesTrackEntity

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(track: FavoritesTrackEntity)

    @Query("SELECT * FROM favorites_table")
    suspend fun getFavorites(): List<FavoritesTrackEntity>

    @Query("SELECT trackId FROM favorites_table")
    suspend fun getFavoritesIds(): List<Int>

    @Delete
    suspend fun deleteFavorite(favoritesTrackEntity: FavoritesTrackEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorites_table WHERE trackId = :trackId)")
    suspend fun isInFavorite(trackId: Int): Boolean
}