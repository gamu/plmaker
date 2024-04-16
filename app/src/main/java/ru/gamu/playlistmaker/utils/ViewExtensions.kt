package ru.gamu.playlistmaker.utils

import android.os.Handler
import android.os.Looper
import android.widget.TextView

fun TextView.setValue(value: String): Unit{
    val handler = Handler(Looper.getMainLooper())
    Thread(Runnable{
        handler.post{ this.text = value }
    }).start()
}