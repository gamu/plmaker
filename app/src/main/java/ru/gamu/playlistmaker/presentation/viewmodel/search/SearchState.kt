package ru.gamu.playlistmaker.presentation.viewmodel.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchState(val showDataLoader: Boolean = false,
                       val searchState: SearchResultState = SearchResultState.NOT_EXECUTED,
                       val isHistoryData: Boolean = false): Parcelable {
    enum class SearchResultState{ SUCCESS, NETWORK_FAILURE, EMPTY_DATASET, NOT_EXECUTED }

    val showNetworErrorView: Boolean
        get() = searchState == SearchResultState.NETWORK_FAILURE

    val showEmptyResultView: Boolean
        get() = searchState == SearchResultState.EMPTY_DATASET

}