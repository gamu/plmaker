package ru.gamu.playlistmaker.presentation.viewmodel.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gamu.playlistmaker.data.models.Response
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.TrackListService

class SearchViewModel(savedStateHandle: SavedStateHandle,
                      private val trackListService: TrackListService): ViewModel()
{
    val searchTokenField = savedStateHandle.getLiveData(Constants.SEARCH_TOKEN_KEY, "")
    val searchResultState = savedStateHandle.getLiveData<SearchState>(Constants.SEARCH_RESULT_STATE_KEY, SearchState.InitialState())
    val searchResultStateValue = savedStateHandle.getLiveData(Constants.SEARCH_RESULT_STATE_VALUE_KEY, listOf<Track>())
    val cleanSearchAvailable = savedStateHandle.getLiveData(Constants.CLEAN_SEARCH_AVAILABLE_KEY, false)

    val trackListMediator = TrackListMediator(searchResultStateValue)

    lateinit var onLoadInPlayer: (track: Track) -> Unit

    init {
        searchTokenField.observeForever { searchToken ->
            if (searchToken.isNotEmpty()) {
                cleanSearchAvailable.value = true
                viewModelScope.launch {
                    delay(Constants.SEARCH_DEBOUNCE_TIMEOUT_MS)
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

    fun onSearchBoxFocusChange() {
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

    suspend fun search(){
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

    object Constants {
        const val SEARCH_DEBOUNCE_TIMEOUT_MS = 2000L
        const val SEARCH_TOKEN_KEY = "searchToken"
        const val SEARCH_RESULT_STATE_KEY = "searchResultState"
        const val SEARCH_RESULT_STATE_VALUE_KEY = "searchResultStateValue"
        const val CLEAN_SEARCH_AVAILABLE_KEY = "cleanSearchAvailable"
    }
}