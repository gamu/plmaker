package ru.gamu.playlistmaker.presentation.viewmodel.playlist.recycler

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.domain.models.Track

class PlaylistEditorViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

    private val trackName = itemView.findViewById<TextView>(R.id.trackName)
    private val artistName = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime = itemView.findViewById<TextView>(R.id.trackTime)
    private val imageTrack = itemView.findViewById<ImageView>(R.id.trackPic)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.requestLayout()
        artistName.text = model.artistName
        trackTime.text = model.formatedTrackTime
        Glide.with(itemView)
            .load(model.artworkUrl)
            .placeholder(R.drawable.img_track_default)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, imageTrack.context)))
            .into(imageTrack)
    }
}

fun dpToPx(dp: Float,context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.resources.displayMetrics).toInt()
}