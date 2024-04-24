package ru.gamu.playlistmaker.presentation.viewholder.tracklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.domain.models.Track

class TrackListAdapter(private val tracks: List<Track>): RecyclerView.Adapter<TrackListViewHolder> () {

    var onClickTrack: ((Track) -> Unit)? = null
    fun onClickTrackTrigger(track: Track) = onClickTrack?.invoke(track)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_track_item, parent, false)
        return TrackListViewHolder(view, ::onClickTrackTrigger)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }


    override fun getItemCount() = tracks.size
}