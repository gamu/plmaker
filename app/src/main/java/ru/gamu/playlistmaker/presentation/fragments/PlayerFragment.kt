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
import ru.gamu.playlistmaker.databinding.FragmentPlayerBinding
import ru.gamu.playlistmaker.presentation.providers.PlayerBundleDataProvider
import ru.gamu.playlistmaker.presentation.viewmodel.player.PlayerViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.player.recycler.TrackInfoAdapter

class PlayerFragment : Fragment() {
    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var adapter: TrackInfoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boundle = getArguments()
        if(boundle != null){
            playerViewModel.setTrackInfo(PlayerBundleDataProvider(boundle))
            adapter = TrackInfoAdapter(playerViewModel.track.getProperies())
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return FragmentPlayerBinding.inflate(inflater, container, false).let{
            val view = it.root
            it.vm = playerViewModel
            it.trackListRecycler.layoutManager = LinearLayoutManager(view.context)
            it.trackListRecycler.adapter = adapter
            playerViewModel.enablePlayback.observe(viewLifecycleOwner){ paying ->
                if(paying){
                    it.btnPause.setBackgroundResource(R.drawable.play)
                }else {
                    it.btnPause.setBackgroundResource(R.drawable.pause)
                }
            }
            playerViewModel.timeLabel.observe(viewLifecycleOwner){ timer ->
                it.lblTiming.text = timer
            }
            it.btnBackToSearch.setOnClickListener {
                findNavController().popBackStack()
            }
            playerViewModel.onPlayerReady {
                it.btnPause.setOnClickListener {
                    playerViewModel.playbackControlPress()
                }
            }
            view
        }
    }

    override fun onStop() {
        super.onStop()
        playerViewModel.stopPlayback()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()
    }
}