package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.FragmentSearchBinding
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.presentation.events.TrackClickListener
import ru.gamu.playlistmaker.presentation.providers.TrackIntentProvider
import ru.gamu.playlistmaker.presentation.viewmodel.search.SearchViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.search.recycler.TrackListAdapter

internal const val BUNDLE_TRACK_KEY ="TRACK"
class SearchFragment : Fragment(), TrackClickListener {
    private val searchViewModel:SearchViewModel by viewModel()
    private val adapter: TrackListAdapter by lazy { TrackListAdapter(this) }
    private var binding: FragmentSearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewModel.trackListMediator.observe(this){ items ->
            items?.let {adapter.setItems(it) }
        }

        searchViewModel.initContent()
        searchViewModel.onLoadInPlayer = ::trackSelectHandler
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentSearchBinding.inflate(inflater, container, false).let{
            binding = it
            it.noConnection.btnRefresh.setOnClickListener{ lifecycleScope.launch { searchViewModel.search() }}
            val view = it.root
            it.btnClearHistory.setOnClickListener{ searchViewModel.cleanHistory() }
            it.vm=searchViewModel
            it.trackListRecycler.layoutManager = LinearLayoutManager(view.context)
            it.trackListRecycler.adapter = adapter
            view
        }
    }

    override fun onStart(){
        super.onStart()
        searchViewModel.onHideHistory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding?.let{
            it.lifecycleOwner = this
        }
    }

    private fun trackSelectHandler(track: Track){
        val bundle = Bundle()
        bundle.putString(BUNDLE_TRACK_KEY, TrackIntentProvider(track).getData())
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment, bundle)
    }

    override fun onTrackClick(track: Track) {
        searchViewModel.trackSelected(track)
    }
}