package ru.gamu.playlistmaker.data.handlers

interface ICommandHandler<T> {
    fun Execute(command: T)
}