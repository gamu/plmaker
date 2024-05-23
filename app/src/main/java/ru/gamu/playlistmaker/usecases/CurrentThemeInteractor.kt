package ru.gamu.playlistmaker.usecases

import android.content.Context
import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository

class CurrentThemeInteractor(context: Context) {
    private var persistentStorage = SettingsPersistentStorageRepository(context)
    fun invoke(): Boolean{
        return persistentStorage.useDarkTheme
    }
}