package ru.gamu.playlistmaker.presentation.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.FragmentPlayerBinding
import ru.gamu.playlistmaker.presentation.viewmodel.player.PlayerViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.player.recycler.TrackInfoAdapter
import ru.gamu.playlistmaker.presentation.viewmodel.playlist.recycler.PlaylistAdapter

class PlayerFragment : Fragment() {
    private val playerViewModel by viewModel<PlayerViewModel> {
        parametersOf(arguments ?: Bundle())
    }

    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var adapter: TrackInfoAdapter
    private lateinit var binding: FragmentPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boundle = getArguments()

        if(boundle != null){
            playerViewModel.setFavorite()
            playerViewModel.initializePlayer()
        }
    }

    override fun onStart() {
        super.onStart()
        playerViewModel.playlist.observe(this){ items ->
            items?.let { playlistAdapter.setItems(items) }
        }

        playerViewModel.getPlaylists()
    }

    private fun hideView(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })
    }

    private fun showView(view: View) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentPlayerBinding.inflate(inflater, container, false).let {
            val bottomSheetBehavior = BottomSheetBehavior.from(it.playlistBottomSheet)
            binding = it
            val view = it.root
            it.vm = playerViewModel
            adapter = TrackInfoAdapter(emptyList())
            playlistAdapter = PlaylistAdapter(playerViewModel)
            it.trackListRecycler.layoutManager = LinearLayoutManager(view.context)
            it.trackListRecycler.adapter = adapter
            it.playListRecycler.layoutManager = LinearLayoutManager(view.context)
            it.playListRecycler.adapter = playlistAdapter
            it.btnFavorite.setOnClickListener {
                playerViewModel.addToFavorite()
            }

            playerViewModel.properties.observe(viewLifecycleOwner) { properties ->
                adapter.updateItems(properties)
            }

            playerViewModel.enablePlayback.observe(viewLifecycleOwner){ paying ->
                if(paying){
                    it.btnPause.setBackgroundResource(R.drawable.play)
                }else {
                    it.btnPause.setBackgroundResource(R.drawable.pause)
                }
            }
            playerViewModel.isFavorite.observe(viewLifecycleOwner){ favorite ->
                if(favorite){
                    it.btnFavorite.setBackgroundResource(R.drawable.favorite)
                }else {
                    it.btnFavorite.setBackgroundResource(R.drawable.not_favorite)
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

            it.btnNewPlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_mediaPlayerFragment_to_newPlayListScreen)
            }

            it.btnPlayList.setOnClickListener {
                //viewModel.getPlayListDb()
                binding.apply {
                    showView(overlay)
                    playlistBottomSheet.isVisible = true
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            it.overlay.setOnClickListener {
                binding.apply {
                    hideView(overlay)
                    playlistBottomSheet.isVisible = false
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    when (newState) {

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            it.overlay.isVisible = false
                        }

                        else -> {
                            it.overlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    //
                }

            })

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