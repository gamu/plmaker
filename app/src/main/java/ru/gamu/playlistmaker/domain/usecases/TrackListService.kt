package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.data.dto.IResponse
import ru.gamu.playlistmaker.data.handlers.ItunesDataQueryAsync
import ru.gamu.playlistmaker.data.handlers.VisitedTracksCommandHandler
import ru.gamu.playlistmaker.data.handlers.VisitedTracksQueryHandler
import ru.gamu.playlistmaker.domain.models.Track

private const val MAX_HISTORY_COUNT = 10
class TrackListService(private val searchQuery: ItunesDataQueryAsync,
                       private val trackPersistentCommand: VisitedTracksCommandHandler,
                       private val trackPersistentQuery: VisitedTracksQueryHandler
) {

    private val tracksHistory = trackPersistentQuery.getData(Unit) as MutableSet

    val HistoryIsNotEmpty: Boolean
        get() = tracksHistory.isNotEmpty()


    fun searchItems(searchToken: String, block:(IResponse<List<Track>>) -> Unit) {
        val result = searchQuery.getData(searchToken)
        block(result)
    }

    fun addTrackToHistory(track: Track) {
        var newHistory = tracksHistory.toMutableList()
        if(newHistory.contains(track)){
            newHistory.remove(track)
            newHistory.add(0, track)
        } else if(tracksHistory.count() >= MAX_HISTORY_COUNT){
            val sliced = newHistory.slice(0..8)
                .toMutableList()
            sliced.add(0, track)
            newHistory = sliced
        } else {
            newHistory.add(0, track)
            newHistory.addAll(newHistory)
        }
        tracksHistory.clear()
        tracksHistory.addAll(newHistory)
        trackPersistentCommand.Execute(tracksHistory)
    }

    fun clearHistory(){
        tracksHistory.clear()
        trackPersistentCommand.Execute(tracksHistory)
    }

    val TracksHistory: Set<Track>
        get() = trackPersistentQuery.getData(Unit)
}