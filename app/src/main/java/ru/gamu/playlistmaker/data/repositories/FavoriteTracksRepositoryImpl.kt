package ru.gamu.playlistmaker.data.repositories

import ru.gamu.playlistmaker.data.db.AppDatabase
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.repository.FavoriteTracksRepository
import ru.gamu.playlistmaker.utils.mapToTrack
import ru.gamu.playlistmaker.utils.toTrackEntity

class FavoriteTracksRepositoryImpl(private val appDatabase: AppDatabase) : FavoriteTracksRepository {

    private val tracksFacade = appDatabase.tracksFacade()

    override suspend fun addTrackToFavorites(track: Track) {
        if(tracksFacade.getTrackById(track.trackId) != null){
            val trackEntity = track.toTrackEntity()
            tracksFacade.addTrackToFavorites(trackEntity)
        }
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        val trackEntity = track.toTrackEntity()
        tracksFacade.removeTrackFromFavorites(trackEntity)
    }

    override suspend fun getAllFavoriteTracks(): List<Track> {
        val result = tracksFacade.getAllFavoriteTracks().map { it.mapToTrack() }
        return result
    }

    override suspend fun getTrackById(trackId: Long): Track? {
        return tracksFacade.getTrackById(trackId)?.mapToTrack()
    }
}