package ru.gamu.playlistmaker.presentation.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.ActivityPlayerBinding
import ru.gamu.playlistmaker.presentation.models.TrackInfo
import ru.gamu.playlistmaker.presentation.viewmodel.player.PlayerViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.player.recycler.TrackInfoAdapter
import ru.gamu.playlistmaker.utils.dsl.getDataBinding
import ru.gamu.playlistmaker.utils.fromJson


class PlayerActivity: AppCompatActivity(){
    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var adapter: TrackInfoAdapter

    private val layoutId: Int by lazy { R.layout.activity_player }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var trackInfo = loadItemFromIntent()
        playerViewModel.setTrackInfo(trackInfo)
        adapter = TrackInfoAdapter(trackInfo.getProperies())

        binding = getDataBinding<ActivityPlayerBinding>(this, layoutId)
            .also {
                it.vm = playerViewModel
                it.trackListRecycler.layoutManager = LinearLayoutManager(this)
                it.trackListRecycler.adapter = adapter
            }

        playerViewModel.enablePlayback.observe(this){ paying ->
            if(paying){
                binding.btnPause.setBackgroundResource(R.drawable.play)
            }else {
                binding.btnPause.setBackgroundResource(R.drawable.pause)
            }
        }
    }
    fun backButtonClick(view: View){
        finish()
    }
    private fun loadItemFromIntent(): TrackInfo {
        val gson = Gson()
        val intent = intent
        val bundle = intent.extras
        val value = bundle?.getString(BUNDLE_TRACK_KEY)
        return gson.fromJson<TrackInfo>(value!!).apply {
            artworkUrl = artworkUrl?.replace("100x100bb", "512x512bb")
        }
    }
    companion object {
        private const val BUNDLE_TRACK_KEY ="TRACK"
    }
}