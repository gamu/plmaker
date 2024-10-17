package ru.gamu.playlistmaker.data.handlers

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gamu.playlistmaker.data.api.ISearchServiceAsync
import ru.gamu.playlistmaker.data.dto.IResponse
import ru.gamu.playlistmaker.data.models.Response
import ru.gamu.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class ItunesDataQueryAsync(val context: Context, val searchService: ISearchServiceAsync) {
    //TODO: Перемещаем ISearchServiceAsync в DI
    //private val searchService: ISearchServiceAsync by inject()

    private fun formatYear(dateString: String): String{
        try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
            val formattedDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date!!)
            return formattedDatesString
        }catch(ex: Exception){
            return ""
        }

    }

    fun getData(spec: String): Flow<IResponse<List<Track>>> = flow {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        try {
            val response = searchService.search(spec)
            if (response.isError) {
                emit(Response.ERROR)
                return@flow
            }
            val result = response.results.map {
                Track(
                    artistName = it.artistName,
                    artworkUrl = it.artworkUrl100,
                    collectionName = it.collectionName,
                    country = it.country,
                    description = it.description,
                    primaryGenreName = it.primaryGenreName,
                    releaseDate = formatYear(it.releaseDate),
                    trackCensoredName = it.trackCensoredName,
                    trackName = it.trackName,
                    trackTimeMs = it.trackTimeMillis,
                    trackPreview = it.previewUrl,
                    trackTime = if (it.trackTimeMillis != null) formatter.format(it.trackTimeMillis) else ""
                )
            }
            if (result.isEmpty()) {
                emit(Response.EMPTY)
            } else {
                emit(Response.SUCCESS.apply { this.setResult(result) })
            }
        } catch (ex: Exception) {
            emit(Response.ERROR.apply { this.setError(ex) })
        }
    }
}