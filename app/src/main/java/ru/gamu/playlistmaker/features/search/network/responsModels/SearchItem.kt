package ru.gamu.playlistmaker.features.search.network.responsModels

data class SearchItem(
    val artistName: String?,
    val artworkUrl100: String,
    val collectionName: String?,
    val country: String,
    val releaseDate: String,
    val description: String?,
    val primaryGenreName: String?,
    val trackName: String?,
    val trackCensoredName: String?,
    val trackTimeMillis: Long?,
    val copyright: String?,
    val contentAdvisoryRating: String?,
    val shortDescription: String?
)