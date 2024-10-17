package ru.gamu.playlistmaker.data.repositories

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import org.koin.java.KoinJavaComponent.inject

class MediaPlayerRepository(val context: Context) {
    private val player: MediaPlayer by inject(MediaPlayer::class.java)

    var setOnCompletionListener: (() -> Unit)? = null
    var setOnPreparedListener: (() -> Unit)? = null
    fun start() {
        player.start()
    }
    fun pause() {
        player.pause()
    }
    fun initializePlayer(trackSource: String) {
        player.setOnCompletionListener { setOnCompletionListener?.invoke() }
        player.setOnPreparedListener { setOnPreparedListener?.invoke() }
        val trackUri = Uri.parse(trackSource)
        player.setDataSource(context, trackUri)
        player.prepareAsync()
    }

    fun stop() {
        player.stop()
        player.release()
    }

    fun position():Int {
        return player.currentPosition
    }

    fun reset() {
        player.reset()
    }

}