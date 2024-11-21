package ru.gamu.playlistmaker.presentation.viewmodel.playlist.recycler

import ru.gamu.playlistmaker.domain.models.Playlist

interface PlaylistClickListener {
    fun onClick(playlist: Playlist)
}