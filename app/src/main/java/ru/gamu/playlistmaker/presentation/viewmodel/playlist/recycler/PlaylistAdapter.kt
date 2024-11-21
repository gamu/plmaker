package ru.gamu.playlistmaker.presentation.viewmodel.playlist.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.PlaylistItemBinding
import ru.gamu.playlistmaker.domain.models.Playlist

class PlaylistAdapter(private val playlistClickListener: PlaylistClickListener):
    RecyclerView.Adapter<PlaylistViewHolder>() {
    private var playlists: List<Playlist> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<PlaylistItemBinding>(inflater, R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(binding, playlistClickListener)
    }

    fun setItems(newItems: List<Playlist>) {
        playlists = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.binder.apply {
            item = playlist
            executePendingBindings()
        }
    }

}