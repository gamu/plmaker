package ru.gamu.playlistmaker.presentation.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.ActivitySearchBinding
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.presentation.viewmodel.sesrch.SearchViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.sesrch.recycler.TrackListAdapter
import ru.gamu.playlistmaker.utils.dsl.getDataBinding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel by viewModel<SearchViewModel>()

    private val adapter: TrackListAdapter by lazy { TrackListAdapter(searchViewModel) }
    private val layoutId: Int by lazy { R.layout.activity_search }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getDataBinding<ActivitySearchBinding>(this, layoutId)
            .also {
                it.vm = searchViewModel
                it.trackListRecycler.layoutManager = LinearLayoutManager(this)
                it.trackListRecycler.adapter = adapter
        }

        searchViewModel.trackListMediator.observe(this){ items ->
            items?.let {adapter.setItems(it) }
        }

        searchViewModel.initContent()
        searchViewModel.onLoadInPlayer = ::trackSelectHandler

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
        searchViewModel.cleanHistory()
    }
}