package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.GetFavoriteTracksInteractor

class FavoritesViewModel(private val savedStateHandle: SavedStateHandle,
                         private val favoriteTracks: GetFavoriteTracksInteractor): ViewModel() {

    val tracksStateValue = savedStateHandle.getLiveData("tracksStateValue", listOf<Track>())

    fun getFavoriteTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            val tracks = favoriteTracks.invoke()
            tracksStateValue.postValue(tracks)
        }
    }

    fun trackSelected(track: Track) {

    }
}