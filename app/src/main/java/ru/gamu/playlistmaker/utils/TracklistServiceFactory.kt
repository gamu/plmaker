package ru.gamu.playlistmaker.utils

import android.content.Context
import ru.gamu.playlistmaker.data.handlers.ItunesDataQueryAsync
import ru.gamu.playlistmaker.data.handlers.VisitedTracksCommandHandler
import ru.gamu.playlistmaker.data.handlers.VisitedTracksQueryHandler
import ru.gamu.playlistmaker.usecases.TrackListService

fun createTracklistService(applicationContext: Context): TrackListService {
    val searchReader = ItunesDataQueryAsync(applicationContext)
    val historyWriter = VisitedTracksCommandHandler(applicationContext)
    val historyReader = VisitedTracksQueryHandler(applicationContext)

    return TrackListService(searchReader, historyWriter, historyReader)
}