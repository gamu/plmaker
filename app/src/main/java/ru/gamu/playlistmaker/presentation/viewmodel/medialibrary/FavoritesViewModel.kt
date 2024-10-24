package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.GetFavoriteTracksInteractor

class FavoritesViewModel(savedStateHandle: SavedStateHandle,
                         private val favoriteTracks: GetFavoriteTracksInteractor): ViewModel() {
    val tracksStateValue = savedStateHandle.getLiveData(Constants.TRACK_STATE_VALUE, listOf<Track>())

    lateinit var onLoadInPlayer: (track: Track) -> Unit

    fun getFavoriteTracks() {
        viewModelScope.launch {
            val tracks = favoriteTracks.invoke()
            tracksStateValue.postValue(tracks)
        }
    }

    fun trackSelected(track: Track){
        onLoadInPlayer(track)
    }

    object Constants {
        const val TRACK_STATE_VALUE = "tracksStateValue"
    }
}