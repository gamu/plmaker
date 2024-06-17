package ru.gamu.playlistmaker.presentation.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.ActivityPlayerBinding
import ru.gamu.playlistmaker.presentation.providers.PlayerBundleDataProvider
import ru.gamu.playlistmaker.presentation.viewmodel.player.PlayerViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.player.recycler.TrackInfoAdapter
import ru.gamu.playlistmaker.utils.dsl.getDataBinding


class PlayerActivity: AppCompatActivity(){
    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var adapter: TrackInfoAdapter

    private val layoutId: Int by lazy { R.layout.activity_player }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerViewModel.setTrackInfo(PlayerBundleDataProvider(intent.extras!!))
        adapter = TrackInfoAdapter(playerViewModel.track!!.getProperies())

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
    override fun onStop() {
        super.onStop()
        playerViewModel.stopPlayback()
    }
    fun backButtonClick(view: View){
        finish()
    }
}
