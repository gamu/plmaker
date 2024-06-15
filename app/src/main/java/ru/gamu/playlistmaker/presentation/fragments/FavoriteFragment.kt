package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.databinding.FragmentFavoriteBinding
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.FavoritsViewModel

class FavoriteFragment : Fragment() {
    private val vm by viewModel<FavoritsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return FragmentFavoriteBinding.inflate(inflater, container, false).let {
            val view = it.root
            it.placeholder.visibility =
                if (vm.showPlaceholder.value!!) View.VISIBLE else View.GONE
            view
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}