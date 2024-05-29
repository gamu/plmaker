package ru.gamu.playlistmaker.presentation.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.ActivitySearchBinding
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.presentation.viewmodel.sesrch.SearchViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.sesrch.SearchViewModel.Companion.searchVm
import ru.gamu.playlistmaker.presentation.viewmodel.sesrch.recycler.TrackListAdapter
import ru.gamu.playlistmaker.utils.dsl.getDataBinding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private val adapter: TrackListAdapter by lazy { TrackListAdapter(viewModel) }
    private val layoutId: Int by lazy { R.layout.activity_search }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = searchVm(this) {
            dslHandler = Handler(Looper.getMainLooper())
        }

        binding = getDataBinding<ActivitySearchBinding>(this, layoutId)
            .also {
                it.vm = viewModel
                it.trackListRecycler.layoutManager = LinearLayoutManager(this)
                it.trackListRecycler.adapter = adapter
        }

        viewModel.trackListMediator.observe(this){ items ->
            items?.let {adapter.setItems(it) }
        }

        viewModel.initContent()
        viewModel.onLoadInPlayer = ::trackSelectHandler

    }

    fun backButtonClick(view: View){
        finish()
    }

    private fun trackSelectHandler(track: Track){
        val gson = Gson()
        val intent = Intent(this, PlayerActivity::class.java)
        val bundle = Bundle()
        val serializedTrack = gson.toJson(track)
        bundle.putString("TRACK", serializedTrack)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun clearHistoryClick(view: View){
        viewModel.cleanHistory()
    }
}