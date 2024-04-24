package ru.gamu.playlistmaker.presentation.viewholder.tracklist

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
import ru.gamu.playlistmaker.domain.models.Track


class TrackListViewHolder(val parentView: View,
                          private val trackViewListener: (Track) -> Unit
) : RecyclerView.ViewHolder(parentView) {
    private val Title: TextView by lazy { parentView.findViewById(R.id.tbTitle) }
    private val Thumbinal: ImageView by lazy { parentView.findViewById(R.id.imgThumb) }
    private val Information: TextView by lazy { parentView.findViewById(R.id.tbInformation) }
    private val Timing: TextView by lazy { parentView.findViewById(R.id.tbTiming) }

    private lateinit var trackItem: Track

    init {
        parentView.setOnClickListener {
            trackViewListener(trackItem)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(track: Track) {
        this.trackItem = track
        this.Title.text = track.trackName
        this.Information.text = track.artistName
        this.Information.hint = track.artistName
        this.Timing.text = track.trackTime
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterInside(), RoundedCorners(16))
        Glide.with(this.parentView)
            .load(track.artworkUrl)
            .placeholder(R.drawable.placeholder)
            .apply(requestOptions).into(this.Thumbinal)
    }
}