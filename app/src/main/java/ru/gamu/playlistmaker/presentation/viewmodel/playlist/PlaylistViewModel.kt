package ru.gamu.playlistmaker.presentation.viewmodel.playlist

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.domain.usecases.GetPlaylistsInteractor
import ru.gamu.playlistmaker.presentation.viewmodel.playlist.models.ViewmodelPlaylistDto


@Parcelize
data class PlaylistViewModelState(val items: List<ViewmodelPlaylistDto> = emptyList()) : Parcelable

class PlaylistViewModel(private val savedStateHandler: SavedStateHandle): ViewModel() {
    private val getPlaylistsInteractor: GetPlaylistsInteractor by inject(
        GetPlaylistsInteractor::class.java)

    val playlistsState = savedStateHandler.getStateFlow(STATE_KEY, PlaylistViewModelState())

    fun loadPlaylists() {
        viewModelScope.launch {
            val result = getPlaylistsInteractor.invoke()
            savedStateHandler[STATE_KEY] = PlaylistViewModelState(result.map {
                ViewmodelPlaylistDto(it.title, it.description, it.cover, it.tracks.count())} )
        }
    }

    companion object {
        const val STATE_KEY  = "state"
    }
}