package ru.gamu.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.gamu.playlistmaker.data.db.entities.PlaylistItem

@Dao
interface PlaylistFacade {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlistItem: PlaylistItem)
    @Delete
    suspend fun removePlaylist(playlistItem: PlaylistItem)
    @Update
    suspend fun updateTrackslaylist(playlist: PlaylistItem)
    @Query("SELECT * FROM playlist ORDER BY playlistId DESC")
    suspend fun getAllPlaylists(): List<PlaylistItem>
    @Query("DELETE FROM playlist")
    suspend fun clean()
    @Query("SELECT * FROM playlist WHERE playlistId = :id")
    suspend fun getPlaylistById(id: Long): PlaylistItem?
}