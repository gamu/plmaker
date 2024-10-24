package ru.gamu.playlistmaker.utils

import com.google.gson.Gson

fun <T> T.stringify(): String {
    val gson = Gson()
    return gson.toJson(this);
}

inline fun <reified T> parseFromJson(value: String?): T{
    checkNotNull(value) { "Parameter cannot be null" }
    val gson = Gson()
    return gson.fromJson<T>(value)
}