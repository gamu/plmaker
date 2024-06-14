package ru.gamu.playlistmaker.data.repositories

import android.media.MediaPlayer
import org.koin.java.KoinJavaComponent.inject

class MediaPlayerRepository() {
    private val player: MediaPlayer by inject(MediaPlayer::class.java)
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
    fun setOnPreparedListener(block: () -> Unit) {
        block()
    }
    fun setOnCompletionListener(block: () -> Unit) {
        block()
    }
    fun stop() {
        player.stop()
        player.release()
    }
    fun position() = player.currentPosition.toLong()
    fun reset() {
        player.reset()
    }

}