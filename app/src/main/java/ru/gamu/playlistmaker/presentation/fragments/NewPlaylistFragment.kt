package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import ru.gamu.playlistmaker.presentation.composed.NewPlaylist

class NewPlaylistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /*requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmationDialog()
                }
            }
        )*/

        return ComposeView(requireContext()).apply {
            setContent {
                NewPlaylist()
            }
        }
    }


    /*private fun showExitConfirmationDialog() {

    }*/
}

@Preview
@Composable
fun PlaylistPreview() {
    NewPlaylist()
}