package ru.gamu.playlistmaker.features.search.persistance

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.gamu.playlistmaker.R
import ru.gamu.plmaker.core.Track
import ru.gamu.plmaker.core.cq.IQueryHandler

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object: TypeToken<T>() {}.type)
class VisitedTracksQueryHandler(context: Context): IQueryHandler<Set<Track>, Unit> {
    private val ViewHistory: String = context.getString(R.string.viewHistory)
    private val ViewHistoryItems: String = context.getString(R.string.viewHistoryItems)
    private val persistentStorage: SharedPreferences
    init{
        persistentStorage = context.getSharedPreferences(ViewHistory,
            AppCompatActivity.MODE_PRIVATE)
    }
    override fun getData(spec: Unit): Set<Track> {
        val gson = Gson()
        val itemsString = persistentStorage.getString(ViewHistoryItems, "")
        if(itemsString!!.isNotEmpty()){
            val tracks = gson.fromJson<Set<Track>>(itemsString)
            return tracks
        }
        return mutableSetOf()
    }
}