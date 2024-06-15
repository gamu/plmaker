package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritsViewModel: ViewModel() {
    private val _showPlaceholder = MutableLiveData(true)
    val showPlaceholder: LiveData<Boolean> = _showPlaceholder
}