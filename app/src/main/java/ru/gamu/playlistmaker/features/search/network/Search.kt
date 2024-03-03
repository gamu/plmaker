package ru.gamu.playlistmaker.features.search.network

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gamu.playlistmaker.features.search.Track
import ru.gamu.playlistmaker.features.search.network.responsModels.ResponseRoot
import java.text.SimpleDateFormat
import java.util.Locale


class SearchRequest {
    private val searchService: ISearchService

    init {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        searchService = retrofit.create(ISearchService::class.java)
    }

    fun makeCall(searchToken: String, success: (values: List<Track>?) -> Unit) {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L)
        searchService.search(searchToken).enqueue(object: Callback<ResponseRoot> {
            override fun onResponse(call: Call<ResponseRoot>, response: Response<ResponseRoot>) {
                val items = response.body()?.results?.let{
                    val mappedResult = mutableListOf<Track>()
                    for (item in it) {
                        if(item.trackName != null){
                            mappedResult.add(Track(
                                item.trackName,
                                item.artistName,
                                formatter.format(item.trackTimeMillis),
                                item.artworkUrl100)
                            )
                        }
                    }
                    mappedResult
                }
                success(items)
            }

            override fun onFailure(call: Call<ResponseRoot>, t: Throwable) {
                success(null)
            }
        })
    }

    companion object {
        val baseUrl = "https://itunes.apple.com"
    }
}