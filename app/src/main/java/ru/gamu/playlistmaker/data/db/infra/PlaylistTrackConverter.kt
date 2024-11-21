package ru.gamu.playlistmaker.data.db.infra

import androidx.room.TypeConverter
import ru.gamu.playlistmaker.data.db.dto.TracksDto
import ru.gamu.playlistmaker.utils.parseFromJson
import ru.gamu.playlistmaker.utils.stringify

class PlaylistTrackConverter {
    @TypeConverter
    fun fromTracksDto(tracks: TracksDto): String {
        return tracks.stringify()
    }
    @TypeConverter
    fun toTracksDto(tracks: String): TracksDto {
        return parseFromJson(tracks)
    }
    @TypeConverter
    fun fromTracksDtoList(tracks: List<TracksDto>): String {
        return tracks.stringify()
    }
    @TypeConverter
    fun toTracksDtoList(tracks: String): List<TracksDto> {
        return parseFromJson(tracks)
    }
}