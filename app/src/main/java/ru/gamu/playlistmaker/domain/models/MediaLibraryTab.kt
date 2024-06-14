package ru.gamu.playlistmaker.domain.models

open class MediaLibraryTab(open var title: String, open var key: TabKeys = TabKeys.Nothing) {
    enum class TabKeys { Favorites, Playlists, Nothing }
}

