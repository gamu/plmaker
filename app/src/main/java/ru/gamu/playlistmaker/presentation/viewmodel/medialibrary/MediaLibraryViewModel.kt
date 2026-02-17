package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary

import androidx.lifecycle.ViewModel
import ru.gamu.playlistmaker.domain.usecases.GetTabsInteractor

class MediaLibraryViewModel(val tabsServices: GetTabsInteractor): ViewModel() {
    val tabs by lazy { tabsServices.invoke() }
}