package ru.gamu.playlistmaker.domain.models

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.domain.repository.FavoriteTracksRepository
import java.io.Serializable

class Track: Serializable {
    @delegate:Transient
    private val favoriteTracksRepository: FavoriteTracksRepository
        by inject(FavoriteTracksRepository::class.java)

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

    suspend fun addToFavorite() {
        favoriteTracksRepository.addTrackToFavorites(this)
    }

    fun isFavoriteAsync(): Deferred<Boolean> = GlobalScope.async {
        favoriteTracksRepository.getTrackById(trackId) != null
    }

    suspend fun removeFromFavorite() {
        favoriteTracksRepository.removeTrackFromFavorites(this)
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