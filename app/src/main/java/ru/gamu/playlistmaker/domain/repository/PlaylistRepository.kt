package ru.gamu.playlistmaker.domain.repository

import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.domain.models.Track

interface PlaylistRepository {
    suspend fun createPlaylist(name: String, description: String, coverUri: String, tracks: List<Track>?)
    suspend fun getAllPlaylists(): List<Playlist>
    suspend fun getPlaylistById(id: Int): Playlist
    suspend fun updatePlaylist(playlist: Playlist)
}