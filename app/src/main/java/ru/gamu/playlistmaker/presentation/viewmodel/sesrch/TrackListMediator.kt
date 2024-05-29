package ru.gamu.playlistmaker.presentation.viewmodel.sesrch

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import ru.gamu.playlistmaker.domain.models.Track

class TrackListMediator: MediatorLiveData<List<Track>>() {
    private val localDataMediator = MutableLiveData<List<Track>>()
    private val remoteDataMediator = MutableLiveData<List<Track>>()

    init{
        addSource(localDataMediator){
            value = it
        }

        addSource(remoteDataMediator){
            value = it
        }
    }

    fun setLocalSource(tracks: List<Track>){
        localDataMediator.value = tracks
    }

    fun setRemoteSource(tracks: List<Track>){
        remoteDataMediator.value = tracks
    }
}