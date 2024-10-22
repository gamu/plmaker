package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.FavoriteTrackItemBinding
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.FavoritesViewModel

class FavoritesAdapter(val viewModel: FavoritesViewModel): RecyclerView.Adapter<FavoritesViewHolder>() {
    private var tracks: List<Track> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<FavoriteTrackItemBinding>(inflater, R.layout.favorite_track_item, parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val track = tracks[position]
        holder.binder.apply {
            trackItem = track
            vm = viewModel
            executePendingBindings()
        }
    }

    fun setItems(newItems: List<Track>) {
        tracks = newItems
        notifyDataSetChanged()
    }
}