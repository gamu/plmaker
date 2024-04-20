package ru.gamu.plmaker.core

interface IResponse<T>{
    fun getError(): Exception?
    fun setError(ex: Exception): Unit
    fun getIsError(): Boolean
    fun getCount(): Int
    fun setResult(result: T): Unit
    fun getResult(): T
}