package ru.gamu.playlistmaker.features.search.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

import ru.gamu.playlistmaker.features.search.network.responsModels.ResponseRoot

interface ISearchService {
    @GET("/search")
    fun search(@Query("term") name: String ): Call<ResponseRoot>
}