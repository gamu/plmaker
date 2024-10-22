package ru.gamu.playlistmaker.domain.repository

import ru.gamu.playlistmaker.domain.models.Track

interface FavoriteTracksRepository {
    suspend fun addTrackToFavorites(track: Track)
    suspend fun removeTrackFromFavorites(track: Track)
    suspend fun getAllFavoriteTracks(): List<Track>
    suspend fun getTrackById(trackId: Long): Track?
}