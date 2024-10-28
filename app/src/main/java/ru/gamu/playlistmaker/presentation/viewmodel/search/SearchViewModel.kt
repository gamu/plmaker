package ru.gamu.playlistmaker.presentation.viewmodel.search

import androidx.lifecycle.LiveData
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
    private val _searchTokenField = savedStateHandle.getLiveData(Constants.SEARCH_TOKEN_KEY, "")
    private val _searchResultState = savedStateHandle.getLiveData<SearchState>(Constants.SEARCH_RESULT_STATE_KEY, SearchState.InitialState())
    private val _searchResultStateValue = savedStateHandle.getLiveData(Constants.SEARCH_RESULT_STATE_VALUE_KEY, listOf<Track>())
    private val _cleanSearchAvailable = savedStateHandle.getLiveData(Constants.CLEAN_SEARCH_AVAILABLE_KEY, false)

    var searchTokenField: String get() = _searchTokenField.value ?: ""
        set(text: String) = _searchTokenField.postValue(text)

    val searchResultState: LiveData<SearchState> get() = _searchResultState
    val cleanSearchAvailable: LiveData<Boolean> get() = _cleanSearchAvailable

    val trackListMediator = TrackListMediator(_searchResultStateValue)

    lateinit var onLoadInPlayer: (track: Track) -> Unit

    init {
        _searchTokenField.observeForever { searchToken ->
            if (searchToken.isNotEmpty()) {
                _cleanSearchAvailable.value = true
                viewModelScope.launch {
                    delay(Constants.SEARCH_DEBOUNCE_TIMEOUT_MS)
                    val newSearchToken = _searchTokenField.value
                    if (searchToken == newSearchToken) {
                        search()
                    }
                }
            } else {
                _cleanSearchAvailable.value = false
                initContent()
            }
        }
    }

    fun initContent() {
        if (_searchResultStateValue.value!!.isNotEmpty()){
            trackListMediator.setRemoteSource((_searchResultStateValue.value!!))
        }
    }

    fun onSearchBoxFocusChange() {
        val historyItems = trackListService.TracksHistory
        historyItems.let {
            trackListMediator.setLocalSource(it.toList())
            if (it.isNotEmpty()) {
                _searchResultState.value = SearchState.HistoryLoadState()
            }
        }

    }

    fun onHideHistory(){
        trackListMediator.setLocalSource(listOf())
        _searchResultState.value = SearchState.InitialState()
    }

    fun trackSelected(track: Track){
        if(!_searchResultState.value!!.isHistoryData){
            trackListService.addTrackToHistory(track)
        }
        onLoadInPlayer(track)
    }
    fun clearSearchBox() = _searchTokenField.postValue("")
    fun cleanHistory() {
        trackListService.clearHistory()
        trackListMediator.setLocalSource(listOf())
        _searchResultState.value = SearchState.InitialState()
    }

    suspend fun search(){
        _searchTokenField.value?.let{ searchToken ->
            _searchResultState.value = SearchState.DataLoading()
            trackListService.searchItems(searchToken).collect { searchResult ->
                when (searchResult) {
                    Response.EMPTY -> {
                        _searchResultState.value = SearchState.EmptyResult()
                    }

                    Response.ERROR -> {
                        _searchResultState.value = SearchState.NetworkFailedResult()
                    }

                    Response.SUCCESS -> {
                        _searchResultState.value = SearchState.SuccessResult()
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