package ru.gamu.playlistmaker.features.search.network

import android.content.Context
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.features.search.network.responsModels.ResponseRoot
import ru.gamu.plmaker.core.Track
import ru.gamu.plmaker.core.cq.IQueryHandler
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale

class ItunesDataQueryAsync(val context: Context) : IQueryHandler<List<Track>?, String> {
    private val searchService: ISearchServiceAsync
    init {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.iTunesSearchUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        searchService = retrofit.create(ISearchServiceAsync::class.java)
    }
    override fun getData(spec: String) = runBlocking {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L)
        val response = async(Dispatchers.IO) {
            try {
                searchService.search(spec)
            }catch (e: IOException) {
                null
            }catch (e: Exception) {
                null
            }
        }
        try {
            val items = response.await()!!.results.let {
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
            return@runBlocking items
        }catch (ex: Exception){
            return@runBlocking null
        }
    }
}