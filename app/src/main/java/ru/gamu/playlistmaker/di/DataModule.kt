package ru.gamu.playlistmaker.di

import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gamu.playlistmaker.data.api.ISearchServiceAsync
import ru.gamu.playlistmaker.data.db.AppDatabase
import ru.gamu.playlistmaker.data.handlers.ItunesDataQueryAsync
import ru.gamu.playlistmaker.data.handlers.VisitedTracksCommandHandler
import ru.gamu.playlistmaker.data.handlers.VisitedTracksQueryHandler
import ru.gamu.playlistmaker.data.repositories.FavoriteTracksRepositoryImpl
import ru.gamu.playlistmaker.domain.repository.FavoriteTracksRepository

val dataModule = module {
    single<ISearchServiceAsync> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ISearchServiceAsync::class.java)
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single<FavoriteTracksRepository> { FavoriteTracksRepositoryImpl( get() ) }
    single<VisitedTracksCommandHandler> {VisitedTracksCommandHandler(androidContext()) }
    single<VisitedTracksQueryHandler> { VisitedTracksQueryHandler(androidContext()) }
    single<ItunesDataQueryAsync> { ItunesDataQueryAsync(androidContext(), get()) }

    factory { Gson() }
}