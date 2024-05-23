package ru.gamu.playlistmaker.presentation.viewmodel.player

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.gamu.playlistmaker.App
import ru.gamu.playlistmaker.presentation.models.TrackInfo
import ru.gamu.playlistmaker.usecases.MediaPlayerManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PlayerViewModel(val app: Application, val mediaPlayer: MediaPlayerManager,
                      val track: TrackInfo): AndroidViewModel(app)
{
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    val enablePlayback = MutableLiveData(true)
    val timeLabel = MutableLiveData(TIMER_INITIAL_VALUE)
    var handler: Handler? = null

    init{
        initializePlayer(track.trackPreview!!)
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
    companion object{
        private const val TIMER_INITIAL_VALUE = "0:00"
        private const val TIMER_FORMAT = "%02d:%02d"
        fun getViewModelFactory(track: TrackInfo): ViewModelProvider.Factory = viewModelFactory {

            initializer {
                val appCtx = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App)
                val mediaPlayer = MediaPlayerManager(MediaPlayer())
                PlayerViewModel(appCtx, mediaPlayer, track)
            }
        }

        inline fun playerVm(activity: AppCompatActivity, track: TrackInfo,
                            block: PlayerViewModel.Builder.() -> Unit) =
            Builder(activity, track).apply(block).build()
    }

    class Builder(val activity: AppCompatActivity, val track: TrackInfo){
        var dslHandler: Handler? = null
        fun build(): PlayerViewModel {
            var vm = ViewModelProvider(activity, PlayerViewModel.getViewModelFactory(track))
                .get(PlayerViewModel::class.java)
            return vm.apply {
                handler = dslHandler
            }
        }
    }
}