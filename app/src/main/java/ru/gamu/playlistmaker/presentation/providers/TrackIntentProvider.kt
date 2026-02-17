package ru.gamu.playlistmaker.presentation.providers

import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.utils.stringify

class TrackIntentProvider(val track: Track) {
    fun getData(): String = track.stringify()
}