package ru.gamu.playlistmaker.presentation.viewmodel.player

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.MediaPlayerManager
import ru.gamu.playlistmaker.presentation.models.DisplayName
import ru.gamu.playlistmaker.utils.parseFromJson
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.jvmErasure

private const val MPS = 1000
private const val SPM = 60

class PlayerViewModel(private val mediaPlayer: MediaPlayerManager,
                      private val bundle: Bundle): ViewModel() {

    private val track: Track by lazy {
        parseFromJson<Track>(bundle.getString(BUNDLE_TRACK_KEY))
    }

    val enablePlayback = MutableLiveData(true)
    val timeLabel = MutableLiveData(TIMER_INITIAL_VALUE)

    var artistName: String = track.artistName
    var artworkUrl: String = track.artworkUrl
    val trackPreview: String = track.trackPreview
    val trackName: String = track.trackName

    val isFavorite = MutableLiveData(false)

    fun setFavorite() = runBlocking {
        isFavorite.value = track.isFavoriteAsync().await()
    }

    //var isFavorite: Boolean = track.isFavoriteAsync().await()

    @DisplayName("Альбом")
    val collectionName: String = track.collectionName
    @DisplayName("Страна")
    val country: String = track.country
    @DisplayName("Год релиза")
    val releaseDate: String = track.releaseDate
    @DisplayName("Жанр")
    val primaryGenreName: String = track.primaryGenreName
    @DisplayName("Длительность")
    val trackTime: String = track.trackTime

    val playerArtworkUrl: String
        get() = artworkUrl.replace("100x100bb", "512x512bb")

    fun addToFavorite()  {
        viewModelScope.launch(Dispatchers.Main) {
            if(isFavorite.value!!){
                track.removeFromFavorite()
                isFavorite.postValue(false)
            }else {
                track.addToFavorite()
                isFavorite.postValue(true)
            }
        }
    }

    fun onPlayerReady(block: () -> Unit) {
        block()
    }

    fun initializePlayer() {
        initializePlayer(track.trackPreview)
    }

    private fun initializePlayer(trackPreview: String) {
        mediaPlayer.onPlayerPrepared = {
            enablePlayback.value = true
        }

        mediaPlayer.onPlayerComplete = {
            enablePlayback.value = true
        }

        mediaPlayer.onPlayerPause = { }

        mediaPlayer.onCounterSignal = ::displayDuration

        mediaPlayer.PreparePlayer(trackPreview)
    }

    private fun displayDuration(duration: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            val seconds = duration / MPS % SPM
            val minutes = duration / MPS / SPM
            val formattedTime = String.format(TIMER_FORMAT, minutes, seconds)
            timeLabel.value = formattedTime
        }
    }

    fun playbackControlPress() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!mediaPlayer.playerState.IsPlaying()) {
                mediaPlayer.Play()
                enablePlayback.postValue(false)
            } else if (mediaPlayer.playerState == MediaPlayerManager.PlayerStates.STATE_PLAYING) {
                mediaPlayer.Pause()
                enablePlayback.postValue(true)
            } else {
                mediaPlayer.Resume()
                enablePlayback.postValue(false)
            }
        }
    }

    fun stopPlayback() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaPlayer.Stop()
        }
    }

    fun getProperies(): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        val properties = this::class.declaredMemberProperties
        properties.filter { it.returnType.jvmErasure == String::class }.forEach {
            val value = it.call(this) as? String
            val displayValue = it.annotations.filterIsInstance<DisplayName>().firstOrNull()?.displayName
            if(!displayValue.isNullOrEmpty() && !value.isNullOrEmpty()){
                result.add(Pair(displayValue, value))
            }
        }
        return result
    }

    companion object {
        private const val TIMER_INITIAL_VALUE = "0:00"
        private const val TIMER_FORMAT = "%02d:%02d"
        private const val BUNDLE_TRACK_KEY = "TRACK"
    }
}