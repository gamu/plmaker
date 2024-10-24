package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.repository.FavoriteTracksRepository

class FavoriteTrackService(private val favoriteTracksRepository: FavoriteTracksRepository) {


    suspend fun addToFavorite(track: Track) {
        favoriteTracksRepository.addTrackToFavorites(track)
    }
    suspend fun isFavorite(track: Track): Boolean  {
        return favoriteTracksRepository.getTrackById(track.trackId) != null
    }
    suspend fun removeFromFavorite(track: Track) {
        favoriteTracksRepository.removeTrackFromFavorites(track)
    }
}