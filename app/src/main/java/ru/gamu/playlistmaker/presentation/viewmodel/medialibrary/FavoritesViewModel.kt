package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.GetFavoriteTracksInteractor

class FavoritesViewModel(savedStateHandle: SavedStateHandle,
                         private val favoriteTracks: GetFavoriteTracksInteractor): ViewModel() {
    private val _tracksStateValue = savedStateHandle.getLiveData(Constants.TRACK_STATE_VALUE, listOf<Track>())

    val tracksStateValue get() = _tracksStateValue

    fun getFavoriteTracks() {
        viewModelScope.launch {
            val tracks = favoriteTracks.invoke()
            _tracksStateValue.postValue(tracks)
        }
    }

    object Constants {
        const val TRACK_STATE_VALUE = "tracksStateValue"
    }
}