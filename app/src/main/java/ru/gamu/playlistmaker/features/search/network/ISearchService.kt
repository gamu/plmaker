package ru.gamu.playlistmaker.features.search.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.gamu.playlistmaker.features.search.network.responsModels.ResponseRoot


interface ISearchServiceAsync {
    @GET("/search")
    suspend fun search(@Query("term") name: String ): ResponseRoot
}