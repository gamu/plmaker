package ru.gamu.playlistmaker.features.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R

class TrackListAdapter(private val tracks: List<Track>): RecyclerView.Adapter<TrackListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_track_item, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount() = tracks.size
}