package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.FragmentFavoriteBinding
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.presentation.events.TrackClickListener
import ru.gamu.playlistmaker.presentation.providers.TrackIntentProvider
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.FavoritesViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.recycler.FavoritesAdapter

class FavoriteFragment : Fragment(), TrackClickListener {
    private val favoriteViewModel by viewModel<FavoritesViewModel>()
    private var binding: FragmentFavoriteBinding? = null
    private val adapter: FavoritesAdapter by lazy { FavoritesAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        favoriteViewModel.tracksStateValue.observe(this){ items ->
            items?.let { adapter.setItems(it) }
        }

        favoriteViewModel.getFavoriteTracks()
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        return FragmentFavoriteBinding.inflate(inflater, container, false).let {
            val view = it.root
            binding = it
            it.vm = favoriteViewModel
            it.trackListRecycler.layoutManager = LinearLayoutManager(view.context)
            it.trackListRecycler.adapter = adapter
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding?.let{ it.lifecycleOwner = this }
    }


    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun onTrackClick(track: Track) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_TRACK_KEY, TrackIntentProvider(track).getData())
        findNavController().navigate(R.id.action_mediaLibraryFragment_to_playerFragment, bundle)
    }
}