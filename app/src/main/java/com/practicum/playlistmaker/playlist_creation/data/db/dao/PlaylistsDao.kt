package com.practicum.playlistmaker.playlist_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlist_creation.data.db.entity.PlaylistEntity

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists_table WHERE playlistId = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistEntity

    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query("UPDATE playlists_table SET numberOfTracks = :numberOfTracks WHERE playlistId = :playlistId")
    suspend fun updateNumberOfTracks(playlistId: Int, numberOfTracks: Int)

    @Query("SELECT numberOfTracks FROM playlists_table WHERE playlistId = :playlistId")
    suspend fun getNumberOfTracks(playlistId: Int): Int
}