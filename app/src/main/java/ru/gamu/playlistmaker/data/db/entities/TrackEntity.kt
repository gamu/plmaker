package ru.gamu.playlistmaker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val trackId: Long,
    val fileUrl: String,
    val coverUrl: String,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val releaseYear: Int,
    val genre: String,
    val country: String,
    val duration: String
)