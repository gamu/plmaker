package ru.gamu.playlistmaker.domain

interface IMediaPlayerManager {
    fun Play()
    fun PreparePlayer(trackSource: String)
    fun Pause()
    fun Stop()
    fun Resume()
}