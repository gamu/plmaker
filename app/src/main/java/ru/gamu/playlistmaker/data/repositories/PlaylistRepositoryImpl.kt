package ru.gamu.playlistmaker.data.repositories

import ru.gamu.playlistmaker.data.db.AppDatabase
import ru.gamu.playlistmaker.data.db.entities.PlaylistItem
import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.models.playlist
import ru.gamu.playlistmaker.domain.repository.PlaylistRepository
import ru.gamu.playlistmaker.utils.toPlaylistItem
import ru.gamu.playlistmaker.utils.toTrack
import ru.gamu.playlistmaker.utils.toTrackDto

class PlaylistRepositoryImpl(appDatabase: AppDatabase): PlaylistRepository {
    private val playlistFacade = appDatabase.playlistFacade()

    suspend override fun createPlaylist(name: String, description: String, coverUri: String, tracks: List<Track>?) {
        lateinit var playList: PlaylistItem
        if(tracks != null) {
            val tracksDto = tracks.map { it.toTrackDto() }
            playList = PlaylistItem(0, name, coverUri, description, tracksDto)
            playlistFacade.createPlaylist(playList)
        } else {
            playList = PlaylistItem(0, name, coverUri, description, listOf())
        }
        playlistFacade.createPlaylist(playList)
    }

    override suspend fun getAllPlaylists(): List<Playlist> {
        var playlists = playlistFacade.getAllPlaylists()
        val result = playlists.map {
            playlist {
                title = it.title
                description = it.description
                cover = it.coverUri ?: ""

                it.tracks.map { t ->
                    track {
                        trackId(t.trackId)
                        artistName(t.artistName)
                        artworkUrl(t.coverUrl)
                        collectionName(t.albumName)
                        country(t.country)
                        releaseDate(t.releaseYear)
                        primaryGenreName(t.genre)
                        trackName(t.trackName)
                        trackTime(t.duration)
                        trackPreview(t.fileUrl)
                    }
                }
            }
        }
        return result
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        playlistFacade.getPlaylistById(id).let {
            return playlist {
                title = it.title
                description = it.description
                cover = it.coverUri ?: ""

                it.tracks.map { track ->
                    track.toTrack()
                }
            }
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistId = playlistFacade.getAllPlaylists().filter { it.title == playlist.title }.first().playlistId
        val playlistItem = playlist.toPlaylistItem(playlistId)
        playlistFacade.updateTrackslaylist(playlistItem)
    }
}