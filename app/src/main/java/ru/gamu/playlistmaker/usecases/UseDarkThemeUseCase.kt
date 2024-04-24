package ru.gamu.playlistmaker.usecases

import android.content.Context
import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository

class UseDarkThemeUseCase(context: Context) {
    private var persistentStorage = SettingsPersistentStorageRepository(context)
    fun invoke(useDarkTheme: Boolean){
        persistentStorage.useDarkTheme = useDarkTheme
    }
}