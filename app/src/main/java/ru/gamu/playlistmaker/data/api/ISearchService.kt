package ru.gamu.playlistmaker.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.gamu.playlistmaker.data.models.ResponseRoot


interface ISearchServiceAsync {
    @GET("/search")
    suspend fun search(@Query("term") name: String ): ResponseRoot
}