package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.repository.PlaylistRepository

class AddTrackToPlaylistInteractor(private val playlistRepository: PlaylistRepository) {
    suspend fun invoke(playlist: Playlist, track: Track): Boolean {
        var playlistEntity = playlistRepository.getAllPlaylists()
            .filter { it.title == playlist.title }
            .first()

        val tracks = playlistEntity.tracks.toMutableList()
        val hasTrack = tracks.any { it.trackId == track.trackId }
        if(!hasTrack){
            tracks.add(track)
            playlistRepository.updatePlaylist(playlist.copy(tracks = tracks))
            return true
        }
        return false
    }
}