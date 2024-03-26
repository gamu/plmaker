package ru.gamu.plmaker.core.cq

interface ICommandHandler<T> {
    fun Execute(command: T)
}