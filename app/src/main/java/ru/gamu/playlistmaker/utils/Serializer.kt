package ru.gamu.playlistmaker.utils

import com.google.gson.Gson

fun <T> T.stringify(): String {
    val gson = Gson()
    return gson.toJson(this);
}

inline fun <reified T> parseFromJson(value: String): T{
    val gson = Gson()
    return gson.fromJson<T>(value)
}