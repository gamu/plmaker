package ru.gamu.playlistmaker.di

import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.gamu.playlistmaker.data.repositories.MediaPlayerRepository
import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository
import ru.gamu.playlistmaker.domain.usecases.CurrentThemeInteractor
import ru.gamu.playlistmaker.domain.usecases.DarkThemeInteractor
import ru.gamu.playlistmaker.domain.usecases.GetTabsInteractor
import ru.gamu.playlistmaker.domain.usecases.MediaPlayerManager
import ru.gamu.playlistmaker.domain.usecases.TrackListService

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
val domainModule = module {
    single<TrackListService> { TrackListService(get(), get(), get()) }
    single<DarkThemeInteractor> { DarkThemeInteractor( get() ) }
    single<CurrentThemeInteractor> { CurrentThemeInteractor( get() ) }
    factory { MediaPlayerRepository() }
    factory<MediaPlayerManager> { MediaPlayerManager() }
    factory { MediaPlayer(androidContext()) }
    factory { GetTabsInteractor() }
    factory { SettingsPersistentStorageRepository(androidContext()) }
}