package com.practicum.playlistmaker.playlist_creation.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlist_creation.data.db.entity.PlaylistTracksCrossRef

@Dao
interface PlaylistTracksCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(trackInPlaylist: PlaylistTracksCrossRef)

    @Query("SELECT * FROM playlist_tracks_cross_ref_table WHERE playlistId = :playlistId")
    suspend fun getTracksInPlaylist(playlistId: Int): List<PlaylistTracksCrossRef>

    @Query("DELETE FROM playlist_tracks_cross_ref_table WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteTrack(trackId: Int, playlistId: Int)

    @Query("SELECT * FROM playlist_tracks_cross_ref_table WHERE trackId = :trackId")
    suspend fun getPlaylistsContainingTrack(trackId: Int): List<PlaylistTracksCrossRef>

    @Query("DELETE FROM playlist_tracks_cross_ref_table WHERE playlistId = :playlistId")
    suspend fun deleteAllTracksInPlaylist(playlistId: Int)
}