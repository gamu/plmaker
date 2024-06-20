package ru.gamu.playlistmaker.domain.usecases

import android.util.Log
import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.data.repositories.MediaPlayerRepository
import ru.gamu.playlistmaker.domain.IMediaPlayerManager
import ru.gamu.playlistmaker.domain.PlaybackControl

class MediaPlayerManager: Thread(), IMediaPlayerManager {
    private val mediaPlayer: MediaPlayerRepository by inject(MediaPlayerRepository::class.java)
    private var mutex = Object()

    @get:Synchronized
    @set:Synchronized
    var playerState = PlayerStates.STATE_DEFAULT

    var onPlayerPrepared: (() -> Unit)? = null
    var onPlayerComplete: (() -> Unit)? = null
    var onCounterSignal: ((mils: Long) -> Unit)? = null
    var onPlayerPause: (() -> Unit)? = null

    override fun run() {
        mediaPlayer.start()
        playerState = PlayerStates.STATE_PLAYING
        synchronized(mutex) {
            currentThread().name
            while(playerState.IsPlaying()){
                sleep(PLAYBACK_SIGNAL_TIMEOUT_MS)
                if(playerState == PlayerStates.STATE_PAUSED){
                    mediaPlayer.pause()
                    mutex.wait()
                }
                if(onCounterSignal != null) {
                    onCounterSignal?.invoke(mediaPlayer.position())
                }
            }
        }
        mediaPlayer.stop()
    }

    override fun PreparePlayer(trackSource: String) {
        try{
            mediaPlayer.setOnPreparedListener = {
                playerState = PlayerStates.STATE_PREPERED
                if(onPlayerPrepared != null){
                    onPlayerPrepared?.invoke()
                }
            }
            mediaPlayer.setOnCompletionListener = {
                playerState = PlayerStates.STATE_FINISHED
                synchronized(mutex){
                    if(onPlayerComplete != null && onCounterSignal != null){
                        onCounterSignal?.invoke(0)
                        onPlayerComplete?.invoke()
                    }
                }
            }
            mediaPlayer.reset()
            mediaPlayer.initializePlayer(trackSource)
        }
        catch(e: Exception){
            Log.d("ERR", e.message!!)
        }
    }

    override fun Pause() {
        playerState = PlayerStates.STATE_PAUSED
        if(onPlayerPause != null){
            onPlayerPause?.invoke()
        }
    }

    override fun Stop() {
        playerState = PlayerStates.STATE_FINISHED
    }

    override fun Resume() {
        synchronized(mutex) {
            mutex.notify()
            mediaPlayer.start()
            playerState = PlayerStates.STATE_PLAYING
        }
    }

    companion object {
        private const val PLAYBACK_SIGNAL_TIMEOUT_MS = 100L
    }

    enum class PlayerStates: PlaybackControl {
        STATE_DEFAULT { override fun IsPlaying(): Boolean = false },
        STATE_PREPERED { override fun IsPlaying(): Boolean = false },
        STATE_PLAYING { override fun IsPlaying(): Boolean = true },
        STATE_PAUSED { override fun IsPlaying(): Boolean = true },
        STATE_FINISHED { override fun IsPlaying(): Boolean = false },
    }
}