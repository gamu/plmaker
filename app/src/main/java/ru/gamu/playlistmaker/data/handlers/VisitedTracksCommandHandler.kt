package ru.gamu.playlistmaker.data.handlers

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.domain.models.Track

class VisitedTracksCommandHandler(val context: Context) {
    private val ViewHistory: String = context.getString(R.string.viewHistory)
    private val ViewHistoryItems: String = context.getString(R.string.viewHistoryItems)
    private val persistentStorage: SharedPreferences
    init{
        persistentStorage = context.getSharedPreferences(ViewHistory,
            AppCompatActivity.MODE_PRIVATE)
    }
    fun Execute(command: Set<Track>) {
        if(command.isEmpty()){
            persistentStorage.edit().remove(ViewHistoryItems).apply()
        }else{
            val gson = Gson()
            val stringify = gson.toJson(command)
            persistentStorage.edit().putString(ViewHistoryItems, stringify).apply()
        }
    }
}