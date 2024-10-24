package ru.gamu.playlistmaker.presentation.events

import ru.gamu.playlistmaker.domain.models.Track

interface TrackClickListener {
    fun onTrackClick(track: Track)
}