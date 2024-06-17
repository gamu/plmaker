package ru.gamu.playlistmaker.utils

import android.util.Log

object Logger {
    fun writeError(e: Exception){
        Log.wtf("ERR", e)
    }
}
