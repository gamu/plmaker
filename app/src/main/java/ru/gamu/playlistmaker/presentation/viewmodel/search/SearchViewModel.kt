package ru.gamu.playlistmaker.presentation.viewmodel.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gamu.playlistmaker.data.models.Response
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.TrackListService

class SearchViewModel(private val savedStateHandle: SavedStateHandle,
                      private val trackListService: TrackListService): ViewModel()
{
    val searchTokenField = savedStateHandle.getLiveData("searchToken", "")
    val searchResultState = savedStateHandle.getLiveData<SearchState>("searchResultState", SearchState.InitialState())
    val searchResultStateValue = savedStateHandle.getLiveData("searchResultStateValue", listOf<Track>())
    val cleanSearchAvailable = savedStateHandle.getLiveData("cleanSearchAvailable", false)

    val trackListMediator = TrackListMediator(searchResultStateValue)

    lateinit var onLoadInPlayer: (track: Track) -> Unit

    init {
        searchTokenField.observeForever { searchToken ->
            if (searchToken.isNotEmpty()) {
                cleanSearchAvailable.value = true
                viewModelScope.launch {
                    delay(SEARCH_DEBOUNCE_TIMEOUT_MS)
                    val newSearchToken = searchTokenField.value
                    if (searchToken == newSearchToken) {
                        search()
                    }
                }
            } else {
                cleanSearchAvailable.value = false
                initContent()
            }
        }
    }

    fun initContent() {
        if (searchResultStateValue.value!!.isNotEmpty()){
            trackListMediator.setRemoteSource((searchResultStateValue.value!!))
        }
    }

    fun onSearchBoxFocusChange(hasFocus: Boolean) {
        val historyItems = trackListService.TracksHistory
        historyItems.let {
            trackListMediator.setLocalSource(it.toList())
            if (it.isNotEmpty()) {
                searchResultState.value = SearchState.HistoryLoadState()
            }
        }

    }

    fun onHideHistory(){
        trackListMediator.setLocalSource(listOf())
        searchResultState.value = SearchState.InitialState()
    }

    fun trackSelected(track: Track){
        if(!searchResultState.value!!.isHistoryData){
            trackListService.addTrackToHistory(track)
        }
        onLoadInPlayer(track)
    }
    fun clearSearchBox() = searchTokenField.postValue("")
    fun cleanHistory() {
        trackListService.clearHistory()
        trackListMediator.setLocalSource(listOf())
        searchResultState.value = SearchState.InitialState()
    }

    private suspend fun search(){
        searchTokenField.value?.let{ searchToken ->
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
        private const val SEARCH_RESULTS_KEY = "SR"
    }
}