package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository

class CurrentThemeInteractor(var persistentStorage: SettingsPersistentStorageRepository) {
    fun invoke(): Boolean{
        return persistentStorage.useDarkTheme
    }
}