package ru.gamu.playlistmaker.features.player

import kotlin.reflect.full.declaredMemberProperties

class TrackInfo(){

    val artistName: String? = null
    val artworkUrl: String? = null

    @DisplayName("Альбом")
    val collectionName: String? = null

    @DisplayName("Страна")
    val country: String? = null

    @DisplayName("Год релиза")
    val releaseDate: String? = null
    val description: String? = null

    @DisplayName("Жанр")
    val primaryGenreName: String? = null
    val trackName: String? = null
    val trackCensoredName: String? = null

    @DisplayName("Длительность")
    val trackTime: String? = null

    fun getProperies(): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        val properties = this::class.declaredMemberProperties
        properties.forEach{
            val value = it.call(this) as? String
            val displayValue = it.annotations.filterIsInstance<DisplayName>().firstOrNull()?.displayName
            if(!displayValue.isNullOrEmpty() && !value.isNullOrEmpty()){
                result.add(Pair(displayValue, value))
            }
        }
        return result
    }
}