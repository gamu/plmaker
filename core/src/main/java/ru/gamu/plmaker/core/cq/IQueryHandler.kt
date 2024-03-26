package ru.gamu.plmaker.core.cq

interface IQueryHandler<T, K> {
    fun getData(spec: K): T
}