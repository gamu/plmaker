package ru.gamu.playlistmaker.features.search.network

import android.content.Context
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gamu.playlistmaker.R
import ru.gamu.plmaker.core.Track
import ru.gamu.plmaker.core.cq.IQueryHandler
import java.io.IOException
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

private fun formatYear(dateString: String): String{
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
        val formattedDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date!!)
        return formattedDatesString
    }
    override fun getData(spec: String) = runBlocking {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
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
                            artistName = item.artistName,
                            artworkUrl = item.artworkUrl100,
                            collectionName = item.collectionName,
                            country = item.country,
                            description = item.description,
                            primaryGenreName = item.primaryGenreName,
                            releaseDate = formatYear(item.releaseDate),
                            trackCensoredName = item.trackCensoredName,
                            trackName = item.trackName,
                            trackTimeMs = item.trackTimeMillis,
                            trackPreview = item.previewUrl,
                            trackTime = formatter.format(item.trackTimeMillis)),
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