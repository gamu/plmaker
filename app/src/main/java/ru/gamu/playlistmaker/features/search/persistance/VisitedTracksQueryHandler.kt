package ru.gamu.playlistmaker.features.search.persistance

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import ru.gamu.playlistmaker.R
import ru.gamu.plmaker.core.Track
import ru.gamu.plmaker.core.cq.IQueryHandler

class VisitedTracksQueryHandler(val context: Context): IQueryHandler<Set<Track>, Unit> {
    private val ViewHistory: String = context.getString(R.string.viewHistory)
    private val ViewHistoryItems: String = context.getString(R.string.viewHistoryItems)
    private val persistentStorage: SharedPreferences
    private val gson = Gson()
    init{
        persistentStorage = context.getSharedPreferences(ViewHistory,
            AppCompatActivity.MODE_PRIVATE)
    }
    override fun getData(spec: Unit): Set<Track> {
        val itemsString = persistentStorage.getStringSet(ViewHistoryItems, setOf())
        val tracks = itemsString!!.map{ gson.fromJson(it, Track::class.java) }.toSet()
        return tracks
    }
}