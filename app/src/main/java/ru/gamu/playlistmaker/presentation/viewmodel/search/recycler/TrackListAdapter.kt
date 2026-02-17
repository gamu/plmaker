package ru.gamu.playlistmaker.presentation.viewmodel.search.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.SearchTrackItemBinding
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.presentation.events.TrackClickListener

class TrackListAdapter(private val trackClickListener: TrackClickListener) : RecyclerView.Adapter<TrackListViewHolder> () {

    private var tracks: List<Track> = listOf()

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): TrackListViewHolder {
        val inflater = LayoutInflater.from(parentView.context)
        val binding = DataBindingUtil.inflate<SearchTrackItemBinding>(inflater, R.layout.search_track_item, parentView, false)
        return TrackListViewHolder(binding, trackClickListener)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val track = tracks[position]
        holder.binder.apply {
            trackItem = track
            executePendingBindings()
        }
    }

    override fun getItemCount() = tracks.size

    fun setItems(newItems: List<Track>) {
        tracks = newItems
        notifyDataSetChanged()
    }
}