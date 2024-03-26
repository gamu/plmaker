package ru.gamu.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import ru.gamu.playlistmaker.features.fromJson
import ru.gamu.playlistmaker.features.player.TrackInfo
import ru.gamu.playlistmaker.features.player.TrackInfoAdapter

class PlayerActivity: AppCompatActivity(){

    val imageViewCover: ImageView by lazy { findViewById(R.id.imgCover) }
    val textViewTrackName: TextView by lazy { findViewById(R.id.txtTrackName) }
    val textViewArtistName: TextView by lazy { findViewById(R.id.txtArtist) }
    val trackListRecyclerView: RecyclerView by lazy { findViewById(R.id.trackListRecycler) }
    val buttonBack: ImageView by lazy { findViewById(R.id.btnBackToSearch) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_view)

        buttonBack.setOnClickListener{
            finish()
        }

        val trackInfo = loadItemFromIntent()
        if(trackInfo != null){
            Glide.with(this)
                .load(trackInfo.artworkUrl?.replace("100x100bb", "512x512bb"))
                .placeholder(R.drawable.placeholder_big)
                .into(imageViewCover)
            textViewTrackName.text = trackInfo.trackName
            textViewArtistName.text = trackInfo.artistName

            val adapter = TrackInfoAdapter(trackInfo.getProperies())
            trackListRecyclerView.layoutManager = LinearLayoutManager(this)
            trackListRecyclerView.adapter = adapter
        }
    }

    private fun loadItemFromIntent(): TrackInfo? {
        val gson = Gson()
        val intent = getIntent()
        val bundle = intent.getExtras()
        if (bundle != null) {
            val value = bundle.getString("TRACK")
            return gson.fromJson<TrackInfo>(value!!)
        }
        return null
    }
}