package ru.gamu.playlistmaker.utils

import ru.gamu.playlistmaker.data.db.entities.TrackEntity
import ru.gamu.playlistmaker.data.dto.SearchItem
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.models.track

fun TrackEntity.mapToTrack(): Track {
    return track {
        artistName(artistName)
        artworkUrl(coverUrl)
        collectionName(albumName)
        country(country)
        releaseDate(releaseYear.toString())
        primaryGenreName(genre)
        trackName(trackName)
        trackTime(duration)
        trackPreview(fileUrl)
        trackTimeMs(0L)
    }
}

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        artistName = this.artistName,
        coverUrl = this.artworkUrl,
        albumName = this.collectionName,
        releaseYear = this.releaseDate.toIntOrNull() ?: 0,
        genre = this.primaryGenreName,
        trackName = this.trackName,
        duration = this.trackTime,
        fileUrl = this.trackPreview,
        country = this.country,
        trackId = this.trackId
    )
}

fun SearchItem.toTrack(): Track {
    return track {
        trackId(trackId)
        artistName(artistName ?: "")
        artworkUrl(artworkUrl100)
        collectionName(collectionName ?: "")
        country(country)
        releaseDate(releaseDate)
        primaryGenreName(primaryGenreName ?: "")
        trackName(trackName ?: "")
        trackTime(trackTimeMillis?.toString() ?: "")
        trackPreview(previewUrl ?: "")
    }
}
