package ru.gamu.playlistmaker.domain.usecases

import android.content.Context
import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository

class DarkThemeInteractor(context: Context) {
    private var persistentStorage = SettingsPersistentStorageRepository(context)
    fun invoke(useDarkTheme: Boolean){
        persistentStorage.useDarkTheme = useDarkTheme
    }
}