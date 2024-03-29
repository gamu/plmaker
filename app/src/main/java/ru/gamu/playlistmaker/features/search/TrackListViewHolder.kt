package ru.gamu.playlistmaker.features.search

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.gamu.playlistmaker.R
import ru.gamu.plmaker.core.Track


class TrackListViewHolder(val parentView: View,
                          private val trackViewListener: (Track) -> Unit
) : RecyclerView.ViewHolder(parentView) {
    private val Title: TextView
    private val Thumbinal: ImageView
    private val Information: TextView

    private lateinit var trackItem: Track

    init {
        this.Title = parentView.findViewById(R.id.tbTitle)
        this.Thumbinal = parentView.findViewById(R.id.imgThumb)
        this.Information = parentView.findViewById(R.id.tbInformation)

        parentView.setOnClickListener {
            trackViewListener(trackItem)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(track: Track) {
        this.trackItem = track
        this.Title.text = track.trackName
        this.Information.text = "${ track.artistName } \u2022 ${ track.trackTime }"
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterInside(), RoundedCorners(16))
        Glide.with(this.parentView)
            .load(track.artworkUrl)
            .placeholder(R.drawable.placeholder)
            .apply(requestOptions).into(this.Thumbinal)
    }
}