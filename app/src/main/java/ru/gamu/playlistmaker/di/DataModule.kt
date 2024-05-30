package ru.gamu.playlistmaker.di

import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gamu.playlistmaker.data.api.ISearchServiceAsync
import ru.gamu.playlistmaker.data.handlers.ItunesDataQueryAsync
import ru.gamu.playlistmaker.data.handlers.VisitedTracksCommandHandler
import ru.gamu.playlistmaker.data.handlers.VisitedTracksQueryHandler

val dataModule = module {
    single<ISearchServiceAsync> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ISearchServiceAsync::class.java)
    }

    single<VisitedTracksCommandHandler> {
        VisitedTracksCommandHandler(androidContext())
    }

    single<VisitedTracksQueryHandler> {
        VisitedTracksQueryHandler(androidContext())
    }

    single<ItunesDataQueryAsync> {
        ItunesDataQueryAsync(androidContext(), get())
    }

    factory { Gson() }
}