package ru.gamu.playlistmaker.domain.usecases

import android.media.MediaPlayer
import ru.gamu.playlistmaker.domain.IMediaPlayerManager
import ru.gamu.playlistmaker.domain.PlaybackControl

class MediaPlayerManager(private val mediaPlayer: MediaPlayer): Thread(), IMediaPlayerManager {
    private var mutex = Object()

    @get:Synchronized
    @set:Synchronized
    var PlayerState = PlayerStates.STATE_DEFAULT

    var onPlayerPrepared: (() -> Unit)? = null
    var onPlayerComplete: (() -> Unit)? = null
    var onCounterSignal: ((mils: Long) -> Unit)? = null
    var onPlayerPause: (() -> Unit)? = null

    override fun run() {
        mediaPlayer.start()
        PlayerState = PlayerStates.STATE_PLAYING
        synchronized(mutex) {
            currentThread().name
            while(PlayerState.IsPlaying()){
                sleep(PLAYBACK_SIGNAL_TIMEOUT_MS)
                if(PlayerState == PlayerStates.STATE_PAUSED){
                    mediaPlayer.pause()
                    mutex.wait()
                }
                if(onCounterSignal != null) {
                    onCounterSignal?.invoke(mediaPlayer.currentPosition.toLong())
                }
            }
        }
    }

    override fun PreparePlayer(trackSource: String) {
        mediaPlayer.setDataSource(trackSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            PlayerState = PlayerStates.STATE_PREPERED
            if(onPlayerPrepared != null){
                onPlayerPrepared?.invoke()
            }
        }
        mediaPlayer.setOnCompletionListener {
            PlayerState = PlayerStates.STATE_FINISHED
            synchronized(mutex){
                if(onPlayerComplete != null && onCounterSignal != null){
                    onCounterSignal?.invoke(0)
                    onPlayerComplete?.invoke()
                }
            }
        }
    }

    override fun Pause() {
        PlayerState = PlayerStates.STATE_PAUSED
        if(onPlayerPause != null){
            onPlayerPause?.invoke()
        }
    }

    override fun Stop() {
        mediaPlayer.stop()
        PlayerState = PlayerStates.STATE_FINISHED
    }

    override fun Resume() {
        synchronized(mutex) {
            mutex.notify()
            mediaPlayer.start()
            PlayerState = PlayerStates.STATE_PLAYING
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