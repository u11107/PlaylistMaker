package com.practicum.playlistmaker.media.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media.data.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(track: PlaylistTrackEntity)

    @Delete
    suspend fun deletePlaylistTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track")
    suspend fun getPlaylistTracks(): List<PlaylistTrackEntity>

    @Query("SELECT * FROM playlist_track WHERE trackId = :trackId")
    suspend fun getPlaylistTrackById(trackId: Long): PlaylistTrackEntity
}