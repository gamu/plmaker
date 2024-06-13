package ru.gamu.playlistmaker.domain.models

open class MediaLibraryTab {
    enum class TabKeys { Favorites, Playlists, Nothing }
    open var title: String = ""
    open var key: TabKeys = TabKeys.Nothing
}

fun MediaLibraryTabFactory(title: String, key: MediaLibraryTab.TabKeys): MediaLibraryTab {
    return MediaLibraryTab().apply {
        this.title = title
        this.key = key
    }
}