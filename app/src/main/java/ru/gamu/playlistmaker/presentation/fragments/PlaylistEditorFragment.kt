package ru.gamu.playlistmaker.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.FragmentPlaylistInfoBinding
import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.presentation.viewmodel.playlist.PlaylistEditorViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.playlist.recycler.PlaylistEditorAdapter
import ru.gamu.playlistmaker.utils.stringify

class PlaylistEditorFragment: Fragment() {

    private val onClickTrack = object : PlaylistEditorAdapter.OnClickTrackListener {
        override fun onClickTrack(track: Track) {
            startPlayer(track)
        }

        override fun onLongClickTrack(track: Track) {
            MaterialAlertDialogBuilder(requireContext(), R.style.PlayListDialogStyle)
                .setTitle(getString(R.string.delete_track))
                .setMessage(getString(R.string.do_you_want_to_delete_track))
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    //
                }.setPositiveButton(getString(R.string.delete)) { _, _ ->
                    viewModel.deleteSelectedTrackFromPlaylist(track)
                }.show()
        }

    }

    private val viewModel by viewModel<PlaylistEditorViewModel>()
    private var _binding: FragmentPlaylistInfoBinding? = null
    private val adapterTrackPlaylist: PlaylistEditorAdapter by lazy { PlaylistEditorAdapter(onClickTrack) }

    private val binding get() = _binding!!

    private val playlistId: Long by lazy { requireArguments().getLong(ARG_PLAYLIST) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackRecyclerView.adapter = adapterTrackPlaylist

        viewModel.getPlaylist(playlistId)
        viewModel.getTracksByPlaylistId(playlistId)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.menuButton.setOnClickListener {
            binding.apply {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.textDeletePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.PlayListDialogStyle
            ).setMessage(getString(R.string.do_you_wont_to_delete_playlist))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                    //
                }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deletePlaylistById(playlistId)
                }.show()
        }

        binding.textEditInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistEditorFragment_to_newPlayListScreen, bundleOf(ARG_PLAYLIST to playlistId)
            )
        }

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { playlist ->
            if(playlist != null){
                renderTracksBottomSheet(playlist)
                renderMenuBottomSheet(playlist)
            }
        }

        viewModel.getListTracks().observe(viewLifecycleOwner) { tracks ->
            if (tracks.isNotEmpty()) {
                adapterTrackPlaylist.listTracksOfPlaylist = tracks
                adapterTrackPlaylist.notifyDataSetChanged()

                binding.buttonShare.setOnClickListener {
                    viewModel.shareLinkPlaylist(playlistId)
                }

                binding.textShare.setOnClickListener {
                    viewModel.shareLinkPlaylist(playlistId)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            } else {
                binding.apply {
                    trackRecyclerView.isVisible = false
                    textMessage.isVisible = true
                }


                binding.buttonShare.setOnClickListener {
                    messageEmptyPlaylist()
                }

                binding.textShare.setOnClickListener {
                    messageEmptyPlaylist()
                }
            }
        }

        viewModel.getStateDelete().observe(viewLifecycleOwner) { deleteState ->
            if (deleteState.isComplete) {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_playlistEditorFragment_to_playerFragment,
                bundleOf(KEY_TRACK to track.stringify())
        )
    }

    private fun messageEmptyPlaylist() {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.playlist_not_have_tracks),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun renderTracksBottomSheet(playlist: Playlist) {
        binding.apply {
            namePlaylist.text = playlist.title
            if (playlist.description.isEmpty()) {
                descriptionPlaylist.isVisible = false
            } else {
                descriptionPlaylist.isVisible = true
                descriptionPlaylist.text = playlist.description
            }
            coverPlaylist.setImageURI(Uri.parse(playlist.cover))
            totalTime.text = getString(R.string.tracks_count, playlist.totalDuration, playlist.getMinuteDeclension())
            amountTracks.text = getString(R.string.tracks_count, playlist.tracksCount, playlist.getTrackDeclension())
        }
    }

    private fun renderMenuBottomSheet(playlist: Playlist) {
        binding.apply {
            if (playlist.cover.isNotEmpty()) {
                coverPlaylistLinear.setImageURI(Uri.parse(playlist.cover))
            }
            namePlaylistLinear.text = playlist.title
            titleTrackLinear.text = getString(R.string.tracks_count, playlist.tracksCount, playlist.getTrackDeclension())
        }
    }

    private fun getYPosition(elem: View): Int {
        val xy = intArrayOf(0, 0)
        elem.getLocationOnScreen(xy)
        return xy[1]
    }

    companion object {
        const val KEY_TRACK = "TRACK"
        const val ARG_PLAYLIST = "playlist"
    }
}