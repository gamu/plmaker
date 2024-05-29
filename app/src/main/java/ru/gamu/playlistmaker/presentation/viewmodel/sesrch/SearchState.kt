package ru.gamu.playlistmaker.presentation.viewmodel.sesrch

sealed class SearchState {
    enum class SearchResultState{ SUCCESS, NETWORK_FAILURE, EMPTY_DATASET, NOT_EXECUTED }
    open val showDataLoader: Boolean = false
    open val searchState: SearchResultState = SearchResultState.NOT_EXECUTED
    open val isHistoryData = false
    val showNetworErrorView: Boolean
        get() = searchState == SearchResultState.NETWORK_FAILURE

    val showEmptyResultView: Boolean
        get() = searchState == SearchResultState.EMPTY_DATASET

    class InitialState: SearchState()
    class HistoryLoadState: SearchState() { override val isHistoryData = true }
    class DataLoading: SearchState() { override val showDataLoader = true  }
    class NetworkFailedResult: SearchState() { override val searchState =
        SearchResultState.NETWORK_FAILURE
    }
    class EmptyResult: SearchState() { override val searchState =
        SearchResultState.EMPTY_DATASET
    }
    class SuccessResult: SearchState() { override val searchState =
        SearchResultState.SUCCESS }
}