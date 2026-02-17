package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.repository.FavoriteTracksRepository

class GetFavoriteTracksInteractor(private val favoriteTracksRepository: FavoriteTracksRepository) {
    suspend fun invoke(): List<Track> {
        return favoriteTracksRepository.getAllFavoriteTracks()
    }
}