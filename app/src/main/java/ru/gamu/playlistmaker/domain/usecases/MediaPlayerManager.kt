package ru.gamu.playlistmaker.domain.usecases

import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.data.repositories.MediaPlayerRepository
import ru.gamu.playlistmaker.domain.IMediaPlayerManager
import ru.gamu.playlistmaker.domain.PlaybackControl
import ru.gamu.playlistmaker.utils.Logger

class MediaPlayerManager: Thread(), IMediaPlayerManager {
    private val mediaPlayer:MediaPlayerRepository by inject(MediaPlayerRepository::class.java)
    private var mutex = Object()

    @get:Synchronized
    @set:Synchronized
    var playerState = PlayerStates.STATE_DEFAULT

    var onPlayerPrepared: (() -> Unit)? = null
    var onPlayerComplete: (() -> Unit)? = null
    var onCounterSignal: ((mils: Long) -> Unit)? = null
    var onPlayerPause: (() -> Unit)? = null

    private val canBeStopped: Boolean
     get() = playerState == PlayerStates.STATE_PLAYING
                || playerState == PlayerStates.STATE_PAUSED

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
    }

    override fun preparePlayer(trackSource: String) {
        try{
            mediaPlayer.reset()
            mediaPlayer.setDataSource(trackSource)
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerStates.STATE_PREPARED
                if(onPlayerPrepared != null){
                    onPlayerPrepared?.invoke()
                }
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerStates.STATE_FINISHED
                synchronized(mutex){
                    if(onPlayerComplete != null && onCounterSignal != null){
                        onCounterSignal?.invoke(0)
                        onPlayerComplete?.invoke()
                    }
                }
            }
        }
        catch(e: IllegalStateException){
            Logger.writeError(e)
        }
    }

    override fun playbackPause() {
        playerState = PlayerStates.STATE_PAUSED
        if(onPlayerPause != null){
            onPlayerPause?.invoke()
        }
    }

    override fun playbackStop() {
        if(canBeStopped){
            mediaPlayer.stop()
        }
        playerState = PlayerStates.STATE_FINISHED
    }

    override fun playbackResume() {
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
        STATE_PREPARED { override fun IsPlaying(): Boolean = false },
        STATE_PLAYING { override fun IsPlaying(): Boolean = true },
        STATE_PAUSED { override fun IsPlaying(): Boolean = true },
        STATE_FINISHED { override fun IsPlaying(): Boolean = false },
    }
}
