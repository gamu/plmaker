package ru.gamu.playlistmaker.data.repositories

import android.media.MediaPlayer
import org.koin.java.KoinJavaComponent.inject

class MediaPlayerRepository {
    private val player: MediaPlayer by inject(MediaPlayer::class.java)

    var setOnCompletionListener: (() -> Unit)? = null
    var setOnPreparedListener: (() -> Unit)? = null
    init{
        player.setOnCompletionListener{ setOnCompletionListener?.invoke() }
        player.setOnPreparedListener { setOnPreparedListener?.invoke() }
    }
    fun start() {
        player.start()
    }
    fun pause() {
        player.pause()
    }
    fun setDataSource(trackSource: String) {
        player.setDataSource(trackSource)
        player.prepareAsync()
    }

    fun stop() {
        player.stop()
        player.release()
    }
    fun position():Long {
        if(player.isPlaying){
            return player.currentPosition.toLong()
        }
        return Long.MIN_VALUE
    }
    fun reset() {
        player.reset()
    }

}