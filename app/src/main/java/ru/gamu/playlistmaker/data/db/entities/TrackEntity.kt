package ru.gamu.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val trackId: Long,
    val fileUrl: String,
    val coverUrl: String,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val releaseYear: String,
    val genre: String,
    val country: String,
    val duration: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val timestamp: Long = System.currentTimeMillis()
)