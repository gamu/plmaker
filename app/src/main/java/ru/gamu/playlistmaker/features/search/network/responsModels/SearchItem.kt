package ru.gamu.playlistmaker.features.search.network.responsModels

data class SearchItem(
    val artistName: String,
    val trackName: String?,
    val artworkUrl100: String,
    val trackTimeMillis: Long?
)