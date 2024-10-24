package ru.gamu.playlistmaker.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gamu.playlistmaker.data.db.AppDatabase
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.repository.FavoriteTracksRepository
import ru.gamu.playlistmaker.utils.mapToTrack
import ru.gamu.playlistmaker.utils.toTrackEntity

class FavoriteTracksRepositoryImpl(appDatabase: AppDatabase) : FavoriteTracksRepository {

    private val tracksFacade = appDatabase.tracksFacade()

    override suspend fun addTrackToFavorites(track: Track) {
        if(tracksFacade.getTrackById(track.trackId) == null){
            val trackEntity = track.toTrackEntity()
            tracksFacade.addTrackToFavorites(trackEntity)
        }
    }

    override suspend fun removeTrackFromFavorites(track: Track) = withContext(Dispatchers.IO) {
        val trackEntity = track.toTrackEntity()
        tracksFacade.removeTrackFromFavorites(trackEntity)
    }

    override suspend fun getAllFavoriteTracks(): List<Track> = withContext(Dispatchers.IO) {
        val result = tracksFacade.getAllFavoriteTracks().map { it.mapToTrack() }
        return@withContext result
    }

    override suspend fun getTrackById(trackId: Long): Track? = withContext(Dispatchers.IO) {
        return@withContext tracksFacade.getTrackById(trackId)?.mapToTrack()
    }
}