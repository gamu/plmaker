package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.recycler

import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.databinding.FavoriteTrackItemBinding
import ru.gamu.playlistmaker.presentation.events.TrackClickListener

// FavoritesViewHolder.kt
class FavoritesViewHolder(
    val binder: FavoriteTrackItemBinding,
    private val trackClickListener: TrackClickListener
) : RecyclerView.ViewHolder(binder.root) {

    init {
        binder.root.setOnClickListener {
            binder.trackItem?.let { track ->
                trackClickListener.onTrackClick(track)
            }
        }
    }
}