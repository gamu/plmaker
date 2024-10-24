package ru.gamu.playlistmaker.presentation.viewmodel.search.recycler

import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.databinding.SearchTrackItemBinding
import ru.gamu.playlistmaker.presentation.events.TrackClickListener


class TrackListViewHolder(val binder: SearchTrackItemBinding,
                          private val trackClickListener: TrackClickListener
) :RecyclerView.ViewHolder(binder.root) {
    init {
        binder.root.setOnClickListener {
            binder.trackItem?.let { track ->
                trackClickListener.onTrackClick(track)
            }
        }
    }
}