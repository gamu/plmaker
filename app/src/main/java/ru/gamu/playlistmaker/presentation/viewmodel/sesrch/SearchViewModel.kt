package ru.gamu.playlistmaker.presentation.viewmodel.sesrch

import android.app.Application
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.gamu.playlistmaker.App
import ru.gamu.playlistmaker.data.models.Response
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.usecases.TrackListService
import ru.gamu.playlistmaker.utils.createTracklistService

class SearchViewModel(val trackListService: TrackListService, val app: Application): AndroidViewModel(app)
{
    val searchResultState = MutableLiveData<SearchState>(SearchState.InitialState())
    var handler: Handler? = null
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
                    handler?.postDelayed(Runnable {
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
                handler?.post{ searchResultState.value = SearchState.DataLoading() }
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
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {

            initializer {
                val appCtx = (this[APPLICATION_KEY] as App)
                val service = createTracklistService(appCtx)
                SearchViewModel(service, appCtx)
            }
        }

        inline fun searchVm(activity: AppCompatActivity, block: Builder.() -> Unit) =
            Builder(activity)
                .apply(block)
                .build()
    }

    class Builder(val activity: AppCompatActivity){
        var dslHandler: Handler? = null
        fun build(): SearchViewModel {
            var vm = ViewModelProvider(activity, getViewModelFactory()).get(SearchViewModel::class.java)
            return vm.apply {
                        handler = dslHandler
                    }
        }
    }
}