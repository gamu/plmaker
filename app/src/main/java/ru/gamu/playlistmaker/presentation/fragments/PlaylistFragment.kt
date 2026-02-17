package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.presentation.composed.Playlist
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.PlaylistViewModel


class PlaylistFragment : Fragment() {
    private val vm by viewModel<PlaylistViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Playlist()
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}