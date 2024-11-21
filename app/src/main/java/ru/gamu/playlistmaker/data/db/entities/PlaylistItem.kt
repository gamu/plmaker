package ru.gamu.playlistmaker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.gamu.playlistmaker.data.db.dto.TracksDto
import ru.gamu.playlistmaker.data.db.infra.PlaylistTrackConverter

@Entity(tableName = "playlist")
@TypeConverters(PlaylistTrackConverter::class)
data class PlaylistItem(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long,
    val title: String,
    val coverUri: String?,
    val description: String,
    val tracks: List<TracksDto>)
