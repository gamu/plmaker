package ru.gamu.playlistmaker.features.search.network.responsModels

data class ResponseRoot(
    val resultCount: Long,
    val results: List<SearchItem>,
)