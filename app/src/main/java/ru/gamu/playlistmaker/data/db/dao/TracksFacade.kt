package ru.gamu.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.gamu.playlistmaker.data.db.entities.TrackEntity

@Dao
interface TracksFacade {

    @Insert
    suspend fun addTrackToFavorites(track: TrackEntity)

    @Delete
    suspend fun removeTrackFromFavorites(track: TrackEntity)

    @Query("SELECT * FROM tracks")
    suspend fun getAllFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks")
    suspend fun getAllFavoriteTrackIds(): List<Int>

    @Query("SELECT * FROM tracks WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?

}