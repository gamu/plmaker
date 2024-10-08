package ru.gamu.playlistmaker.di


import androidx.lifecycle.SavedStateHandle
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.FavoritsViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.MediaLibraryViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.PlaylistViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.player.PlayerViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.sesrch.SearchViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.settings.SettingsViewModel

val viewModelModule = module {
    viewModel<SearchViewModel> { (handle: SavedStateHandle) ->
        SearchViewModel( handle )
    }

    viewModel<SettingsViewModel>{
        SettingsViewModel(get(), get(), androidContext())
    }

    viewModel<PlayerViewModel>{
        PlayerViewModel( get() )
    }

    viewModel<MediaLibraryViewModel>{
        MediaLibraryViewModel( get() )
    }

    viewModel<PlaylistViewModel>{
        PlaylistViewModel()
    }

    viewModel<FavoritsViewModel>{
        FavoritsViewModel()
    }
}