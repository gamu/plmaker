package ru.gamu.playlistmaker.presentation.viewmodel.sesrch

import android.os.Handler
import android.os.Looper
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.data.models.Response
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.TrackListService

class SearchViewModel: ViewModel()
{
    val trackListService: TrackListService by inject(TrackListService::class.java)
    val searchResultState = MutableLiveData<SearchState>(SearchState.InitialState())
    val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    val trackListMediator = TrackListMediator()
    var searchTokenField = ObservableField<String>()
    var cleanSearchAvailable = MutableLiveData(false)

    lateinit var onLoadInPlayer: (track: Track) -> Unit

    init {
        searchTokenField.set("")
        searchTokenField.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val searchToken = searchTokenField.get() ?: ""
                if(searchToken.isNotEmpty()){
                    cleanSearchAvailable.value = true
                    viewModelScope.launch {
                        delay(SEARCH_DEBOUNCE_TIMEOUT_MS)
                        val newSearchToken = searchTokenField.get()
                        if(searchToken == newSearchToken){
                            search()
                        }
                    }
                }else{
                    cleanSearchAvailable.value = false
                    initContent()
                }
            }
        })
    }

    fun initContent() {
        val historyItems = trackListService.TracksHistory
        historyItems.let {
            trackListMediator.setLocalSource(historyItems.toList())
            if (it.isNotEmpty()) searchResultState.value = SearchState.HistoryLoadState()
        }
    }

    fun trackSelected(track: Track){
        if(!searchResultState.value!!.isHistoryData){
            trackListService.addTrackToHistory(track)
        }
        onLoadInPlayer(track)
    }
    fun clearSearchBox() = searchTokenField.set("")
    fun cleanHistory() {
        trackListService.clearHistory()
        trackListMediator.setLocalSource(listOf())
        searchResultState.value = SearchState.InitialState()
    }

    private suspend fun search(){
        searchTokenField.get()?.let{ searchToken ->
            searchResultState.value = SearchState.DataLoading()
            trackListService.searchItems(searchToken).collect { searchResult ->
                when (searchResult) {
                    Response.EMPTY -> {
                        searchResultState.value = SearchState.EmptyResult()
                    }

                    Response.ERROR -> {
                        searchResultState.value = SearchState.NetworkFailedResult()
                    }

                    Response.SUCCESS -> {
                        searchResultState.value = SearchState.SuccessResult()
                        trackListMediator.setRemoteSource(Response.SUCCESS.getResult())
                    }

                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIMEOUT_MS = 2000L
    }
}