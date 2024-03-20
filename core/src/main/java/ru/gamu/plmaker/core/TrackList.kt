package ru.gamu.plmaker.core

import ru.gamu.plmaker.core.cq.ICommandHandler
import ru.gamu.plmaker.core.cq.IQueryHandler

private const val MAX_HISTORY_COUNT = 10
class TrackList(private val searchQuery: IQueryHandler<List<Track>?, String>,
                private val trackPersistentCommand: ICommandHandler<Set<Track>>,
                private val trackPersistentQuery: IQueryHandler<Set<Track>, Unit>) {

    private val tracksHistory = trackPersistentQuery.getData(Unit) as MutableSet

    val HistoryIsNotEmpty: Boolean
        get() = tracksHistory.isNotEmpty()


    fun searchItems(searchToken: String): List<Track>? {
        val result = searchQuery.getData(searchToken)
        if(result == null){
            return null
        }else if(result.isNotEmpty()) {
            val trackViewHistory = trackPersistentQuery.getData(Unit)
            val diff = result - trackViewHistory
            val intersect = trackViewHistory.intersect(result)
            return intersect.toList() + diff.toList()
        }
        return listOf()
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