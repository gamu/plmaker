package ru.gamu.playlistmaker.domain.models

interface ITrack {
    val artistName: String?
    val artworkUrl: String?
    val collectionName: String?
    val country: String?
    val releaseDate: String?
    val description: String?
    val primaryGenreName: String?
    val trackName: String?
    val trackCensoredName: String?
    val trackTime: String?
    val trackPreview: String?
    val trackTimeMs: Long?
}