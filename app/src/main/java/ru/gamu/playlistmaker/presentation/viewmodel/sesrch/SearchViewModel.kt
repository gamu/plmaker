package ru.gamu.playlistmaker.presentation.viewmodel.sesrch

import android.os.Handler
import android.os.Looper
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.data.models.Response
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.TrackListService

class SearchViewModel(): ViewModel()
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
                    handler.postDelayed(Runnable {
                        val newSearchToken = searchTokenField.get()
                        if(searchToken == newSearchToken){
                            search{ thread -> thread.start() }
                        }
                    }, SEARCH_DEBOUNCE_TIMEOUT_MS)
                }else{
                    cleanSearchAvailable.value = false
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
    private fun search(block: (searchThread: Thread) -> Unit){
        searchTokenField.get()?.let{ searchToken ->
            val thread = Thread {
                handler.post{ searchResultState.value = SearchState.DataLoading() }
                trackListService.searchItems(searchToken){
                    when(it){
                        Response.EMPTY -> handler?.post{ searchResultState.value = SearchState.EmptyResult() }
                        Response.ERROR -> handler?.post{ searchResultState.value = SearchState.NetworkFailedResult() }
                        Response.SUCCESS -> handler?.post{
                                searchResultState.value = SearchState.SuccessResult()
                                trackListMediator.setRemoteSource(Response.SUCCESS.getResult())
                        }
                    }
                }
            }
            block(thread);
        }
    }
    companion object {
        private val SEARCH_DEBOUNCE_TIMEOUT_MS = 2000L
    }
}