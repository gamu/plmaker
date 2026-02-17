package ru.gamu.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository
import ru.gamu.playlistmaker.di.dataModule
import ru.gamu.playlistmaker.di.domainModule
import ru.gamu.playlistmaker.di.viewModelModule

class App : Application() {
    var darkTheme = false
    private lateinit var persistentStorage: SettingsPersistentStorageRepository
    override fun onCreate() {
        super.onCreate()

        PermissionRequester.initialize(applicationContext)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule, viewModelModule, domainModule)
        }
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