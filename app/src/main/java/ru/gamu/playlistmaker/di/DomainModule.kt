package ru.gamu.playlistmaker.di

import android.media.MediaPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.gamu.playlistmaker.data.repositories.MediaPlayerRepository
import ru.gamu.playlistmaker.data.repositories.SettingsPersistentStorageRepository
import ru.gamu.playlistmaker.domain.usecases.AddTrackToPlaylistInteractor
import ru.gamu.playlistmaker.domain.usecases.CreatePlaylistInteractor
import ru.gamu.playlistmaker.domain.usecases.CurrentThemeInteractor
import ru.gamu.playlistmaker.domain.usecases.DarkThemeInteractor
import ru.gamu.playlistmaker.domain.usecases.FavoriteTrackService
import ru.gamu.playlistmaker.domain.usecases.GetFavoriteTracksInteractor
import ru.gamu.playlistmaker.domain.usecases.GetPlaylistsInteractor
import ru.gamu.playlistmaker.domain.usecases.GetTabsInteractor
import ru.gamu.playlistmaker.domain.usecases.MediaPlayerManager
import ru.gamu.playlistmaker.domain.usecases.TrackListService

val domainModule = module {
    single<TrackListService> { TrackListService(get(), get(), get()) }
    single<DarkThemeInteractor> { DarkThemeInteractor( get() ) }
    single<CurrentThemeInteractor> { CurrentThemeInteractor( get() ) }
    single<GetPlaylistsInteractor> { GetPlaylistsInteractor( get() ) }
    single<GetFavoriteTracksInteractor> { GetFavoriteTracksInteractor( get() ) }
    single<AddTrackToPlaylistInteractor> { AddTrackToPlaylistInteractor( get() ) }
    single<CreatePlaylistInteractor> { CreatePlaylistInteractor( get() ) }
    single<FavoriteTrackService> { FavoriteTrackService( get() ) }
    factory { MediaPlayerRepository(androidContext()) }
    factory<MediaPlayerManager> { MediaPlayerManager( get() ) }
    factory { MediaPlayer() }
    factory { GetTabsInteractor() }
    factory { SettingsPersistentStorageRepository(androidContext()) }
}