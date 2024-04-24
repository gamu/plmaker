package ru.gamu.playlistmaker.presentation.viewholder.trackinfo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.R

class TrackViewHolder(val parentView: View)
    : RecyclerView.ViewHolder(parentView) {

        val displayView: TextView by lazy { parentView.findViewById(R.id.lblDisplayName) }
        val valueView: TextView by lazy { parentView.findViewById(R.id.lblValue) }

        fun bind(displayName: String, value: String){
            displayView.text = displayName
            valueView.text = value
        }
}