package ru.gamu.playlistmaker.presentation.viewmodel.playlist.recycler

import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.databinding.PlaylistItemBinding

class PlaylistViewHolder(val binder: PlaylistItemBinding,
    private val playlistClickListene: PlaylistClickListener): RecyclerView.ViewHolder(binder.root) {
    init {
        binder.root.setOnClickListener {
            binder.item?.let { item ->
                playlistClickListene.onClick(item)
            }
        }
    }
}