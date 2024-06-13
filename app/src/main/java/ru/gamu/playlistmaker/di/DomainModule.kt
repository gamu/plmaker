package ru.gamu.playlistmaker.di

import android.media.MediaPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.gamu.playlistmaker.domain.usecases.CurrentThemeInteractor
import ru.gamu.playlistmaker.domain.usecases.DarkThemeInteractor
import ru.gamu.playlistmaker.domain.usecases.GetTabsInteractor
import ru.gamu.playlistmaker.domain.usecases.MediaPlayerManager
import ru.gamu.playlistmaker.domain.usecases.TrackListService

val domainModule = module {
    single<TrackListService> { TrackListService(get(), get(), get()) }
    single<DarkThemeInteractor> { DarkThemeInteractor(androidContext()) }
    single<MediaPlayerManager> { MediaPlayerManager( get() ) }
    single<CurrentThemeInteractor> { CurrentThemeInteractor(androidContext()) }
    factory { GetTabsInteractor(androidContext()) }
    factory { MediaPlayer() }
}