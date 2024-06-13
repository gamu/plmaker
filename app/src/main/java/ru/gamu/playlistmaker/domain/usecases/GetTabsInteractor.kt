package ru.gamu.playlistmaker.domain.usecases

import android.content.Context
import ru.gamu.playlistmaker.domain.models.MediaLibraryTab
import ru.gamu.playlistmaker.domain.models.MediaLibraryTabFactory

class GetTabsInteractor(context: Context) {
    fun invoke(): List<MediaLibraryTab>{
        return listOf(MediaLibraryTabFactory("Избранные треки", MediaLibraryTab.TabKeys.Favorites),
            MediaLibraryTabFactory("Плейлисты", MediaLibraryTab.TabKeys.Playlists))
    }
}