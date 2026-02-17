package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository

class DarkThemeInteractor(var persistentStorage: SettingsPersistentStorageRepository) {
    fun invoke(useDarkTheme: Boolean){
        persistentStorage.useDarkTheme = useDarkTheme
    }
}