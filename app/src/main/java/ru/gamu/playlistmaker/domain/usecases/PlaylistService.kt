package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.repository.PlaylistRepository

class PlaylistService(private val playlistRepository: PlaylistRepository) {
    suspend fun getPlaylist(playlistId: Long) = playlistRepository.getPlaylistById(playlistId)
    suspend fun getAllPlaylists() = playlistRepository.getAllPlaylists()
    suspend fun getTracks(playlistId: Long): List<Track> {
        playlistRepository.getPlaylistById(playlistId)?.let{
            return it.tracks
        }
        return listOf()
    }
    suspend fun deleteTrackAndUpdatePlaylist(playlistId: Long, trackId: Long): Playlist? {
        playlistRepository.getPlaylistById(playlistId)?.let { playlist ->
            val updatedTracks = playlist.tracks.filter { it.trackId != trackId }
            val updatedPlaylist = playlist.copy(tracks = updatedTracks)
            try{
                playlistRepository.updatePlaylist(updatedPlaylist)
            } catch (e: IllegalArgumentException) {
                return null
            }
            return updatedPlaylist
        }
        return null
    }
    suspend fun deletePlaylistById(playlistId: Long) {
        playlistRepository.deletePlaylistById(playlistId)
    }
    suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }
}