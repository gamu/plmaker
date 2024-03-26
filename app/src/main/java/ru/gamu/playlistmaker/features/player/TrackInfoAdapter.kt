package ru.gamu.playlistmaker.features.player

import TrackViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R

class TrackInfoAdapter(val trackInfo: List<Pair<String, String>>): RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_info_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = trackInfo.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackInfo[position]
        holder.bind(track.first, track.second)
    }
}