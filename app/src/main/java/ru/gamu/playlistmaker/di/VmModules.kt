package ru.gamu.playlistmaker.di


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.FavoritesViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.MediaLibraryViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.PlaylistViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.player.PlayerViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.playlist.NewPlaylistViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.search.SearchViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.settings.SettingsViewModel

val viewModelModule = module {
    factory { (fragment: Fragment) ->
        fragment.arguments ?: Bundle()
    }

    viewModel<NewPlaylistViewModel> { (handle: SavedStateHandle) ->
        NewPlaylistViewModel( handle )
    }

    viewModel<SearchViewModel> { (handle: SavedStateHandle) ->
        SearchViewModel( handle, get() )
    }

    viewModel<SettingsViewModel>{
        SettingsViewModel(get(), get(), androidContext())
    }

    viewModel<PlayerViewModel>{ (bundle: Bundle) ->
        PlayerViewModel( get(), get(), get(), get(), bundle)
    }

    viewModel<MediaLibraryViewModel>{
        MediaLibraryViewModel( get() )
    }

    viewModel<PlaylistViewModel>{
        PlaylistViewModel()
    }

    viewModel<FavoritesViewModel>{ (handle: SavedStateHandle) ->
        FavoritesViewModel(handle, get())
    }
}