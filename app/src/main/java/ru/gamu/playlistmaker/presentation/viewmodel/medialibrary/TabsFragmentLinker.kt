package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary

import androidx.fragment.app.Fragment
import ru.gamu.playlistmaker.domain.models.MediaLibraryTab

class TabsFragmentLinker(val mediaLibraryTab: MediaLibraryTab): MediaLibraryTab() {
    var fragment: Fragment? = null
}

fun linkFragment(mediaLibraryTab: MediaLibraryTab, fragment: Fragment): TabsFragmentLinker {
    return TabsFragmentLinker(mediaLibraryTab).apply {
        this.fragment = fragment
    }
}