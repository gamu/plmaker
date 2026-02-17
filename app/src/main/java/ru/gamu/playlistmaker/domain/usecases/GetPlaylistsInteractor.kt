package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.domain.repository.PlaylistRepository

class GetPlaylistsInteractor(private val playlistRepository: PlaylistRepository) {
    suspend fun invoke(): List<Playlist> {
        return playlistRepository.getAllPlaylists()
    }
}