package ru.gamu.playlistmaker.domain.models

import java.io.Serializable

class Track: Serializable {
    var trackId: Long = 0L
    var artistName: String = ""
    var artworkUrl: String = ""
    var collectionName: String = ""
    var releaseDate: String = ""
    var primaryGenreName: String = ""
    var trackName: String = ""
    var trackTime: String = ""
    var trackPreview: String = ""
    var country: String = ""

    val formatedTrackTime: String
        get() = formatMilliseconds()

    private fun formatMilliseconds(): String {
        val millis = trackTime.toLong()
        val hours = (millis / 1000) / 3600
        val minutes = ((millis / 1000) % 3600) / 60
        val seconds = (millis / 1000) % 60
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}

class TrackBuilder {
    private var trackId: Long = 0L
    private var artistName: String = ""
    private var artworkUrl: String = ""
    private var collectionName: String = ""
    private var releaseDate: String = ""
    private var primaryGenreName: String = ""
    private var trackName: String = ""
    private var trackTime: String = ""
    private var trackPreview: String = ""
    private var country: String = ""
    private var trackTimeMs: Long = 0L

    fun trackId(id: Long) {
        this.trackId = id
    }

    fun artistName(name: String) {
        this.artistName = name
    }

    fun artworkUrl(url: String) {
        this.artworkUrl = url
    }

    fun collectionName(name: String) {
        this.collectionName = name
    }

    fun country(name: String) {
        this.country = name
    }

    fun releaseDate(date: String) {
        this.releaseDate = date
    }

    fun primaryGenreName(name: String) {
        this.primaryGenreName = name
    }

    fun trackName(name: String) {
        this.trackName = name
    }

    fun trackTime(time: String) {
        this.trackTime = time
    }

    fun trackPreview(preview: String) {
        this.trackPreview = preview
    }

    fun trackTimeMs(timeMs: Long) {
        this.trackTimeMs = timeMs
    }

    fun build(): Track {
        return Track().apply {
            this.trackId = this@TrackBuilder.trackId
            this.artistName = this@TrackBuilder.artistName
            this.artworkUrl = this@TrackBuilder.artworkUrl
            this.collectionName = this@TrackBuilder.collectionName
            this.releaseDate = this@TrackBuilder.releaseDate
            this.primaryGenreName = this@TrackBuilder.primaryGenreName
            this.trackName = this@TrackBuilder.trackName
            this.trackTime = this@TrackBuilder.trackTime
            this.trackPreview = this@TrackBuilder.trackPreview
        }
    }
}

fun track(init: TrackBuilder.() -> Unit): Track {
    return TrackBuilder().apply(init).build()
}