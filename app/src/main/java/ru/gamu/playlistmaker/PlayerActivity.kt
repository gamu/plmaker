package ru.gamu.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import ru.gamu.playlistmaker.features.fromJson
import ru.gamu.playlistmaker.features.player.MediaPlayerManager
import ru.gamu.playlistmaker.features.player.TrackInfo
import ru.gamu.playlistmaker.features.player.TrackInfoAdapter
import ru.gamu.playlistmaker.utils.setValue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class PlayerActivity: AppCompatActivity(){

    private val imageViewCover: ImageView by lazy { findViewById(R.id.imgCover) }
    private val textViewTrackName: TextView by lazy { findViewById(R.id.txtTrackName) }
    private val textViewArtistName: TextView by lazy { findViewById(R.id.txtArtist) }
    private val textTiming: TextView by lazy { findViewById(R.id.lblTiming) }
    private val trackListRecyclerView: RecyclerView by lazy { findViewById(R.id.trackListRecycler) }
    private val buttonBack: ImageView by lazy { findViewById(R.id.btnBackToSearch) }
    private val buttonPlay: ImageView by lazy { findViewById(R.id.btnPause) }
    private val trackInfo: TrackInfo by lazy { loadItemFromIntent() }

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val mediaPlayer = MediaPlayerManager(MediaPlayer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_view)

        buttonPlay.isEnabled = false

        buttonPlay.setOnClickListener{ playButtonClick() }
        buttonBack.setOnClickListener{ finish() }

        Glide.with(this)
            .load(trackInfo.artworkUrl?.replace("100x100bb", "512x512bb"))
            .placeholder(R.drawable.placeholder_big)
            .into(imageViewCover)
        textViewTrackName.text = trackInfo.trackName
        textViewArtistName.text = trackInfo.artistName

        val adapter = TrackInfoAdapter(trackInfo.getProperies())
        trackListRecyclerView.layoutManager = LinearLayoutManager(this)
        trackListRecyclerView.adapter = adapter

        if(trackInfo.trackPreview != null) {
            initializePlayer(trackInfo.trackPreview!!)
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.Pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.Stop()
    }

    private fun initializePlayer(trackPreview: String) {
        mediaPlayer.onPlayerPrepared = {
            buttonPlay.isEnabled = true
        }
        mediaPlayer.onPlayerComplete = {
            buttonPlay.setBackgroundResource(R.drawable.play)
            textTiming.setValue(TIMER_INITIAL_VALUE)
        }
        mediaPlayer.onPlayerPause = {
            buttonPlay.setBackgroundResource(R.drawable.play)
        }

        mediaPlayer.onCounterSignal = ::displayDuration

        mediaPlayer.PreparePlayer(trackPreview)
    }

    private fun loadItemFromIntent(): TrackInfo {
        val gson = Gson()
        val intent = intent
        val bundle = intent.extras
        val value = bundle?.getString(BUNDLE_TRACK_KEY)
        return gson.fromJson<TrackInfo>(value!!)
    }

    //SimpleDateFormater
    private fun displayDuration(duration: Long) {
        val seconds = duration / 1000 % 60
        val minutes = duration / 1000 / 60
        val formattedTime = String.format(TIMER_FORMAT, minutes, seconds)
        textTiming.text = formattedTime
    }

    private fun playButtonClick() {
        if(!mediaPlayer.PlayerState.IsPlaying()){
            executorService.execute(mediaPlayer)
            buttonPlay.setBackgroundResource(R.drawable.pause)
        } else if(mediaPlayer.PlayerState == MediaPlayerManager.PlayerStates.STATE_PLAYING){
            mediaPlayer.Pause()
        } else {
            mediaPlayer.Resume()
            buttonPlay.setBackgroundResource(R.drawable.pause)
        }
    }

    companion object {
        private const val TIMER_INITIAL_VALUE = "00:00"
        private const val TIMER_FORMAT = "%02d:%02d"
        private const val BUNDLE_TRACK_KEY ="TRACK"
    }
}