package ru.gamu.playlistmaker.presentation.viewmodel.playlist.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.domain.models.Track

class PlaylistEditorAdapter(private val playlistClickListener: OnClickTrackListener):
    RecyclerView.Adapter<PlaylistEditorViewHolder>() {

    var listTracksOfPlaylist: List<Track> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistEditorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_editor_item, parent, false)
        return PlaylistEditorViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return listTracksOfPlaylist.size
    }

    override fun onBindViewHolder(holder: PlaylistEditorViewHolder, position: Int) {
        holder.bind(listTracksOfPlaylist[position])
        holder.itemView.setOnClickListener { playlistClickListener.onClickTrack(listTracksOfPlaylist[position]) }
        holder.itemView.setOnLongClickListener {
            playlistClickListener.onLongClickTrack(listTracksOfPlaylist[position])
            return@setOnLongClickListener true
        }
    }

    interface OnClickTrackListener {
        fun onClickTrack(track: Track)
        fun onLongClickTrack(track: Track)
    }
}