package ru.gamu.playlistmaker.presentation.viewmodel.playlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewmodelPlaylistDto(val title: String,
                                val description: String,
                                val cover: String,
                                val tracksCount: Int) : Parcelable