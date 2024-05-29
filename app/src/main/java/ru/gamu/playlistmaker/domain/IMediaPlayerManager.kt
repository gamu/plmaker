package ru.gamu.playlistmaker.domain

interface IMediaPlayerManager {
    fun run()
    fun PreparePlayer(trackSource: String)
    fun Pause()
    fun Stop()
    fun Resume()
}