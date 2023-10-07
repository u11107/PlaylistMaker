package com.practicum.playlistmaker.playlists_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.practicum.playlistmaker.playlists_creation.data.db.entity.PlaylistEntity

@Dao
interface PlaylistsDao {

    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists_table WHERE playlistId = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistEntity

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("UPDATE playlists_table SET numberOfTracks = :numberOfTracks WHERE playlistId = :playlistId")
    suspend fun updateNumberOfTracks(playlistId: Int, numberOfTracks: Int)
}