package ru.gamu.playlistmaker.data.db.dto

data class TracksDto(val trackId: Long,
                     val fileUrl: String,
                     val coverUrl: String,
                     val trackName: String,
                     val artistName: String,
                     val albumName: String,
                     val releaseYear: String,
                     val genre: String,
                     val country: String,
                     val duration: String,
                     val timestamp: Long = System.currentTimeMillis()) {
}