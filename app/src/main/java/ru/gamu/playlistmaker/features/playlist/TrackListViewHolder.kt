package ru.gamu.playlistmaker.features.playlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.gamu.playlistmaker.R


class TrackListViewHolder(val parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val Title: TextView
    private val Thumbinal: ImageView
    private val Information: TextView

    init {
        this.Title = parentView.findViewById(R.id.tbTitle)
        this.Thumbinal = parentView.findViewById(R.id.imgThumb)
        this.Information = parentView.findViewById(R.id.tbInformation)
    }

    fun bind(track: Track) {
        this.Title.text = track.trackName
        this.Information.text = "${ track.artistName } \u25CF ${ track.trackTime }"
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterInside(), RoundedCorners(16))
        Glide.with(this.parentView)
            .load(track.artworkUrl)
            .apply(requestOptions).into(this.Thumbinal)
    }
}