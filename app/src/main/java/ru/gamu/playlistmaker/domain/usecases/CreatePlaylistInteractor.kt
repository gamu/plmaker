package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.repository.PlaylistRepository

class CreatePlaylistInteractor(private val playlistRepository: PlaylistRepository) {
    suspend fun invoke(name: String, description: String, coverUri: String, tracks: List<Track>?) {
        playlistRepository.createPlaylist(name, description, coverUri, tracks)
    }
}