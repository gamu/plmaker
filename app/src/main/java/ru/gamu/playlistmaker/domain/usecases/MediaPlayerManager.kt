package ru.gamu.playlistmaker.domain.usecases

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.data.repositories.MediaPlayerRepository
import ru.gamu.playlistmaker.domain.IMediaPlayerManager
import ru.gamu.playlistmaker.domain.PlaybackControl


class MediaPlayerManager(private val mediaPlayer: MediaPlayerRepository) : IMediaPlayerManager {
    //private val mediaPlayer: MediaPlayerRepository by inject(MediaPlayerRepository::class.java)
    //private val scope = CoroutineScope(Dispatchers.IO + Job())
    private val scope: CoroutineScope by inject(CoroutineScope::class.java)

    @Volatile
    var playerState = PlayerStates.STATE_DEFAULT

    var onPlayerPrepared: (() -> Unit)? = null
    var onPlayerComplete: (() -> Unit)? = null
    var onCounterSignal: ((mils: Long) -> Unit)? = null
    var onPlayerPause: (() -> Unit)? = null

    suspend fun play() {
        scope.launch {
            mediaPlayer.start()
            playerState = PlayerStates.STATE_PLAYING
            while (playerState == PlayerStates.STATE_PLAYING) {
                delay(PLAYBACK_SIGNAL_TIMEOUT_MS)
                var position = mediaPlayer.position()
                Log.i("MediaPlayerManager", "Current timing: $position")
                if(position > 0)
                    onCounterSignal?.invoke(position.toLong())
            }
        }
    }

    override fun PreparePlayer(trackSource: String) {
        try {
            mediaPlayer.setOnPreparedListener = {
                playerState = PlayerStates.STATE_PREPARED
                onPlayerPrepared?.invoke()
            }
            mediaPlayer.setOnCompletionListener = {
                playerState = PlayerStates.STATE_FINISHED
                onCounterSignal?.invoke(0)
                onPlayerComplete?.invoke()
            }
            mediaPlayer.reset()
            mediaPlayer.initializePlayer(trackSource)
        } catch (e: Exception) {
            Log.d("ERR", e.message!!)
        }
    }

    override fun Play() {
        scope.launch {
            play()
        }
    }

    override fun Pause() {
        scope.launch {
            playerState = PlayerStates.STATE_PAUSED
            onPlayerPause?.invoke()
            mediaPlayer.pause()
        }
    }

    override fun Stop() {
        mediaPlayer.stop()
        playerState = PlayerStates.STATE_FINISHED
        //scope1.cancel()
    }

    override fun Resume() {
        scope.launch {
            play()
        }
    }

    companion object {
        private const val PLAYBACK_SIGNAL_TIMEOUT_MS = 300L
    }

    enum class PlayerStates : PlaybackControl {
        STATE_DEFAULT { override fun IsPlaying(): Boolean = false },
        STATE_PREPARED { override fun IsPlaying(): Boolean = false },
        STATE_PLAYING { override fun IsPlaying(): Boolean = true },
        STATE_PAUSED { override fun IsPlaying(): Boolean = true },
        STATE_FINISHED { override fun IsPlaying(): Boolean = false },
    }
}