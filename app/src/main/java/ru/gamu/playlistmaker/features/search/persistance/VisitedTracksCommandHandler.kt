package ru.gamu.playlistmaker.features.search.persistance

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import ru.gamu.playlistmaker.R
import ru.gamu.plmaker.core.Track
import ru.gamu.plmaker.core.cq.ICommandHandler

class VisitedTracksCommandHandler(val context: Context): ICommandHandler<Set<Track>> {
    private val ViewHistory: String = context.getString(R.string.viewHistory)
    private val ViewHistoryItems: String = context.getString(R.string.viewHistoryItems)
    private val persistentStorage: SharedPreferences
    private val gson = Gson()
    init{
        persistentStorage = context.getSharedPreferences(ViewHistory,
            AppCompatActivity.MODE_PRIVATE)
    }
    override fun Execute(command: Set<Track>) {
        val stringify = command.map { gson.toJson(it) }.toSet()
        persistentStorage.edit().putStringSet(ViewHistoryItems, stringify).apply()
    }
}