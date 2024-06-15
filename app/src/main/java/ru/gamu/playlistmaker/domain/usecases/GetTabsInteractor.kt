package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.models.MediaLibraryTab

class GetTabsInteractor {
    fun invoke(): List<MediaLibraryTab>{
        return listOf(MediaLibraryTab(FAVORITE_TITLE, MediaLibraryTab.TabKeys.Favorites),
            MediaLibraryTab(PLAYLISTS_TITLE, MediaLibraryTab.TabKeys.Playlists))
    }

    companion object {
        val FAVORITE_TITLE = "Избранные треки"
        val PLAYLISTS_TITLE = "Плейлисты"
    }
}