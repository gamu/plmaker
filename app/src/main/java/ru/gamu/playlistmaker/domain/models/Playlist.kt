package ru.gamu.playlistmaker.domain.models

data class Playlist(
    val title: String,
    val description: String,
    val cover: String,
    val tracks: List<Track> = listOf()
)

class PlaylistBuilder {
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
        return Playlist(title, description, cover, tracks)
    }
}

fun playlist(init: PlaylistBuilder.() -> Unit): Playlist {
    val builder = PlaylistBuilder()
    builder.init()
    return builder.build()
}