package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.databinding.FragmentPlaylistBinding
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.PlaylistViewModel


class PlaylistFragment : Fragment() {
    private val vm by viewModel<PlaylistViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return FragmentPlaylistBinding.inflate(inflater, container, false).let {
            val view = it.root
            it.placeholder.visibility =
                if (vm.showPlaceholder.value!!) View.VISIBLE else View.GONE
            view
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}