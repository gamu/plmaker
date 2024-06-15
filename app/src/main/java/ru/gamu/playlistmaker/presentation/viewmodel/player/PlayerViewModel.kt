package ru.gamu.playlistmaker.presentation.viewmodel.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gamu.playlistmaker.domain.usecases.MediaPlayerManager
import ru.gamu.playlistmaker.presentation.models.TrackInfo
import ru.gamu.playlistmaker.presentation.providers.PlayerBundleDataProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PlayerViewModel(val mediaPlayer: MediaPlayerManager): ViewModel()
{
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    var track: TrackInfo? = null

    val enablePlayback = MutableLiveData(true)
    val timeLabel = MutableLiveData(TIMER_INITIAL_VALUE)
    var handler: Handler = Handler(Looper.getMainLooper())

    fun setTrackInfo(trackDataProvider: PlayerBundleDataProvider){
        this.track = trackDataProvider.getData(BUNDLE_TRACK_KEY).apply {
            artworkUrl = artworkUrl?.replace("100x100bb", "512x512bb")
        }
        initializePlayer(track!!.trackPreview!!)
    }
    private fun initializePlayer(trackPreview: String) {

        mediaPlayer.onPlayerPrepared = {
            enablePlayback.value = true
        }

        mediaPlayer.onPlayerComplete = {
            enablePlayback.value = true
        }

        mediaPlayer.onPlayerPause = {
            //buttonPlay.setBackgroundResource(R.drawable.play)
        }

        mediaPlayer.onCounterSignal = ::displayDuration

        mediaPlayer.PreparePlayer(trackPreview)
    }
    private fun displayDuration(duration: Long) {
        val seconds = duration / 1000 % 60
        val minutes = duration / 1000 / 60
        val formattedTime = String.format(TIMER_FORMAT, minutes, seconds)
        handler?.post{ timeLabel.value = formattedTime }
    }
    fun playbackControlPress() {
        if(!mediaPlayer.PlayerState.IsPlaying()){
            executorService.execute(mediaPlayer)
            enablePlayback.value = false
        } else if(mediaPlayer.PlayerState == MediaPlayerManager.PlayerStates.STATE_PLAYING){
            mediaPlayer.Pause()
            enablePlayback.value = true
        } else {
            mediaPlayer.Resume()
            enablePlayback.value = false
        }
    }

    fun stopPlayback() {
        mediaPlayer.Stop()

    }

    companion object{
        private const val TIMER_INITIAL_VALUE = "0:00"
        private const val TIMER_FORMAT = "%02d:%02d"
        private const val BUNDLE_TRACK_KEY ="TRACK"
    }
}