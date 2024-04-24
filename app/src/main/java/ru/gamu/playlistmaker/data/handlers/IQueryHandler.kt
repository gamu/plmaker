package ru.gamu.playlistmaker.data.handlers

interface IQueryHandler<T, K> {
    fun getData(spec: K): T
}