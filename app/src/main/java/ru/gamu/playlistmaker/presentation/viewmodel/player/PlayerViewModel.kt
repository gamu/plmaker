package ru.gamu.playlistmaker.presentation.viewmodel.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gamu.playlistmaker.domain.usecases.MediaPlayerManager
import ru.gamu.playlistmaker.presentation.models.TrackInfo
import ru.gamu.playlistmaker.presentation.providers.PlayerBundleDataProvider

private const val MPS = 1000
private const val SPM = 60

class PlayerViewModel(private val mediaPlayer: MediaPlayerManager): ViewModel() {

    var track: TrackInfo = TrackInfo()
    var formattedTime: String = ""

    val enablePlayback = MutableLiveData(true)
    val timeLabel = MutableLiveData(TIMER_INITIAL_VALUE)
    var handler: Handler = Handler(Looper.getMainLooper())

    fun onPlayerReady(block: () -> Unit) {
        block()
    }

    fun setTrackInfo(trackDataProvider: PlayerBundleDataProvider) {
        track = trackDataProvider.getData(BUNDLE_TRACK_KEY).apply {
            artworkUrl = artworkUrl?.replace("100x100bb", "512x512bb")
        }
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

    companion object {
        private const val TIMER_INITIAL_VALUE = "0:00"
        private const val TIMER_FORMAT = "%02d:%02d"
        private const val BUNDLE_TRACK_KEY = "TRACK"
    }
}