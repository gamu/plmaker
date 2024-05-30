package ru.gamu.playlistmaker.di


import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.gamu.playlistmaker.presentation.viewmodel.player.PlayerViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.sesrch.SearchViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.settings.SettingsViewModel

val viewModelModule = module {
    viewModel<SearchViewModel> {
        SearchViewModel()
    }

    viewModel<SettingsViewModel>{
        SettingsViewModel(get(), get(), androidContext())
    }

    viewModel<PlayerViewModel>{
        PlayerViewModel( get() )
    }
}