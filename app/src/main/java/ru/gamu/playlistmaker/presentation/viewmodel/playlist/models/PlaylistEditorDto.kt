package ru.gamu.playlistmaker.presentation.viewmodel.playlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.gamu.playlistmaker.domain.models.Track

@Parcelize
data class PlaylistEditorDto(val title: String,
                             val description: String,
                             val cover: String,
                             val tracks: List<Track> = listOf()): Parcelable