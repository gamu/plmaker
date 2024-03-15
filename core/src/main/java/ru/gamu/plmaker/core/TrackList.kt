package ru.gamu.plmaker.core

import ru.gamu.plmaker.core.cq.ICommandHandler
import ru.gamu.plmaker.core.cq.IQueryHandler

private const val MAX_HISTORY_COUNT = 10
class TrackList(private val searchQuery: IQueryHandler<List<Track>?, String>,
                private val trackPersistentCommand: ICommandHandler<Set<Track>>,
                private val trackPersistentQuery: IQueryHandler<Set<Track>, Unit>) {

    private val tracksHistory = trackPersistentQuery.getData(Unit) as MutableSet
    fun searchItems(searchToken: String): List<Track>? {
        val result = searchQuery.getData(searchToken)
        if(result !== null && result.count() > 0) {
            val trackViewHistory = trackPersistentQuery.getData(Unit)
            val sumSet = trackViewHistory + result
            return sumSet.toList()
        }
        return null;
    }

    fun addTrackToHistory(track: Track) {
        if(tracksHistory.count() < MAX_HISTORY_COUNT){
            if(tracksHistory.contains(track)){
                tracksHistory.remove(track)
                val newHistory = mutableSetOf(track)
                newHistory.addAll(tracksHistory)
                tracksHistory.clear()
                tracksHistory.addAll(newHistory)
                trackPersistentCommand.Execute(tracksHistory)
            }else {
                tracksHistory.add(track)
                trackPersistentCommand.Execute(tracksHistory)
            }
        }
    }

    fun clearHistory(){
        tracksHistory.clear()
        trackPersistentCommand.Execute(tracksHistory)
    }

    val TracksHistory: Set<Track>
        get() = trackPersistentQuery.getData(Unit)
}