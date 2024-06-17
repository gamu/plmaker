package ru.gamu.playlistmaker.domain

interface IMediaPlayerManager {
    fun preparePlayer(trackSource: String)
    fun playbackPause()
    fun playbackStop()
    fun playbackResume()
}
