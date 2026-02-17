package ru.gamu.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Long = -1,
    val title: String,
    val description: String,
    val cover: String,
    val tracks: List<Track> = listOf()
): Parcelable {
    fun getTrackDeclension(): String {
        val count = tracks.count()
        return when {
            count % 100 in 11..19 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
    }

    fun getMinuteDeclension(): String {
        val minutes: Int = totalDuration
        return when {
            minutes % 100 in 11..19 -> "минут"
            minutes % 10 == 1 -> "минута"
            minutes % 10 in 2..4 -> "минуты"
            else -> "минут"
        }
    }

    val tracksCount: Int
        get() = tracks.size

    val totalDuration: Int
        get() = tracks.sumOf { it.trackTime.toInt() } / 60

    companion object {
        val GetEmptyPlaylist = Playlist(-1, "", "", "", listOf())
    }
}

class PlaylistBuilder {
    var playlistId: Long = -1
    var title: String = ""
    var description: String = ""
    var cover: String = ""
    private val tracks: MutableList<Track> = mutableListOf()

    fun track(init: TrackBuilder.() -> Unit) {
        val trackBuilder = TrackBuilder()
        trackBuilder.init()
        tracks.add(trackBuilder.build())
    }

    fun build(): Playlist {
        return Playlist(playlistId, title, description, cover, tracks)
    }
}

fun playlist(init: PlaylistBuilder.() -> Unit): Playlist {
    val builder = PlaylistBuilder()
    builder.init()
    return builder.build()
}