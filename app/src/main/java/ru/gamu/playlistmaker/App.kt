package ru.gamu.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository

class App : Application() {
    var darkTheme = false
    private lateinit var persistentStorage: SettingsPersistentStorageRepository
    override fun onCreate() {
        super.onCreate()
        persistentStorage = SettingsPersistentStorageRepository(this.applicationContext)
        switchTheme(persistentStorage.useDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}