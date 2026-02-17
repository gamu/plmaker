package ru.gamu.playlistmaker.presentation.viewmodel.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.PlaylistService
import ru.gamu.playlistmaker.domain.usecases.SharingService


class PlaylistEditorViewModel(private val savedStateHandler: SavedStateHandle,
                              private val sharingService: SharingService,
                              private val playlistService: PlaylistService): ViewModel() {

    companion object {
        const val TRACKS_KEY = "tracksKey"
        const val PLAYLIS_KEY = "playlistKey"
    }

    class DeleteState(val isComplete: Boolean)

    private val listTracksState = savedStateHandler.getLiveData<List<Track>>(TRACKS_KEY)
    private val playlistState = savedStateHandler.getLiveData(PLAYLIS_KEY, Playlist.GetEmptyPlaylist)


    fun getListTracks(): LiveData<List<Track>> = listTracksState
    fun getPlaylistState(): LiveData<Playlist> = playlistState

    val playlistTitle get() = playlistState.value?.title ?: ""

    private var stateDeleteLiveData = MutableLiveData<DeleteState>()
    fun getStateDelete(): LiveData<DeleteState> = stateDeleteLiveData

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistService.getPlaylist(playlistId).let { playlist ->
                playlistState.value = playlist
                listTracksState.postValue(playlist?.tracks?.reversed())
            }
        }
    }

    fun getTracksByPlaylistId(playlistId: Long) {
        viewModelScope.launch {
            playlistService.getTracks(playlistId).let { listTrackOfPlaylist ->
                if (listTrackOfPlaylist.isEmpty()) {
                    listTracksState.value = listOf()
                } else {
                    listTracksState.value = listTrackOfPlaylist
                }
            }
        }
    }

    fun deleteSelectedTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistState.value?.let {
                playlistService.deleteTrackAndUpdatePlaylist(it.playlistId, track.trackId)?.let {
                    playlistState.postValue(it)
                    listTracksState.postValue(it.tracks.reversed())
                }
            }
        }
    }

    fun shareLinkPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val message = buildMessage(playlistId)
            sharingService.share(message)
        }
    }

    private suspend fun buildMessage(playlistId: Long): String {
        var textMessage = ""
        viewModelScope.async {
            playlistService.getPlaylist(playlistId)?.let { playlist ->
                textMessage =
                    "${playlist.title}\n${playlist.description}\n${playlist.tracksCount} ${playlist.getTrackDeclension()}"
                playlist.tracks.forEachIndexed { indexTrack, track ->
                    textMessage += "\n${indexTrack}. ${track.artistName} - ${track.trackName} (${track.formatedTrackTime})"
                }
            }
        }.await()
        return textMessage
    }

    fun deletePlaylistById(playlistId: Long) {
        viewModelScope.launch {
            playlistService.deletePlaylistById(playlistId)
            stateDeleteLiveData.postValue(DeleteState(true))
        }
    }
}