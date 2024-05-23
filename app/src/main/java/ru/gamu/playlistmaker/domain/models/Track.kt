package ru.gamu.playlistmaker.domain.models

data class Track(
    override val artistName: String?,
    override val artworkUrl: String?,
    override val collectionName: String?,
    override val country: String?,
    override val releaseDate: String?,
    override val description: String?,
    override val primaryGenreName: String?,
    override val trackName: String?,
    override val trackCensoredName: String?,
    override val trackTime: String?,
    override val trackPreview: String?,
    override val trackTimeMs: Long?
) : ITrack