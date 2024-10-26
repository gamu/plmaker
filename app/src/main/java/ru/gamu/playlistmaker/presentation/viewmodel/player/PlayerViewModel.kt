package ru.gamu.playlistmaker.presentation.viewmodel.player

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.domain.usecases.FavoriteTrackService
import ru.gamu.playlistmaker.domain.usecases.MediaPlayerManager
import ru.gamu.playlistmaker.presentation.models.DisplayName
import ru.gamu.playlistmaker.utils.parseFromJson
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.jvmErasure

private const val MPS = 1000
private const val SPM = 60

class PlayerViewModel(private val mediaPlayer: MediaPlayerManager,
                      private val favoriteTrackService: FavoriteTrackService,
                      private val bundle: Bundle): ViewModel() {

    private val track: Track by lazy {
        parseFromJson<Track>(bundle.getString(BUNDLE_TRACK_KEY))
    }

    private val _enablePlayback = MutableLiveData(true)
    private val _timeLabel = MutableLiveData(TIMER_INITIAL_VALUE)
    private val _properties = MutableLiveData<List<Pair<String, String>>>()

    val enablePlayback: LiveData<Boolean> get() = _enablePlayback
    val timeLabel: LiveData<String> get() = _timeLabel
    val properties: LiveData<List<Pair<String, String>>> get() = _properties


    var artistName: String = track.artistName
    var artworkUrl: String = track.artworkUrl
    val trackName: String = track.trackName

    val isFavorite = MutableLiveData(false)

    fun setFavorite() = viewModelScope.launch(Dispatchers.Main) {
        isFavorite.value = favoriteTrackService.isFavorite(track)
    }

    //var isFavorite: Boolean = track.isFavoriteAsync().await()

    @DisplayName("Альбом")
    val collectionName: String = track.collectionName
    @DisplayName("Страна")
    val country: String = track.country
    @DisplayName("Год релиза")
    val releaseDate: String = extractYear(track.releaseDate)
    @DisplayName("Жанр")
    val primaryGenreName: String = track.primaryGenreName
    @DisplayName("Длительность")
    val trackTime: String = track.formatedTrackTime

    val playerArtworkUrl: String
        get() = artworkUrl.replace("100x100bb", "512x512bb")

    init {
        GlobalContext.get().declare(viewModelScope)
        _properties.postValue(getProperies())
    }

    fun addToFavorite()  {
        viewModelScope.launch(Dispatchers.Main) {
            if(isFavorite.value!!){
                favoriteTrackService.removeFromFavorite(track)
                isFavorite.postValue(false)
            } else {
                favoriteTrackService.addToFavorite(track)
                isFavorite.postValue(true)
            }
        }
    }

    fun onPlayerReady(block: () -> Unit) {
        block()
    }

    fun initializePlayer() {
        initializePlayer(track.trackPreview)
    }

    fun extractYear(dateTimeString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = dateFormat.parse(dateTimeString)
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return yearFormat.format(date)
    }

    private fun initializePlayer(trackPreview: String) {
        mediaPlayer.onPlayerPrepared = {
            _enablePlayback.value = true
        }

        mediaPlayer.onPlayerComplete = {
            _enablePlayback.value = true
        }

        mediaPlayer.onPlayerPause = { }

        mediaPlayer.onCounterSignal = ::displayDuration

        mediaPlayer.PreparePlayer(trackPreview)
    }

    private fun displayDuration(duration: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            val seconds = duration / MPS % SPM
            val minutes = duration / MPS / SPM
            val formattedTime = String.format(TIMER_FORMAT, minutes, seconds)
            _timeLabel.value = formattedTime
        }
    }

    fun playbackControlPress() {
        if (!mediaPlayer.playerState.IsPlaying()) {
            mediaPlayer.Play()
            _enablePlayback.postValue(false)
        } else if (mediaPlayer.playerState == MediaPlayerManager.PlayerStates.STATE_PLAYING) {
            mediaPlayer.Pause()
            _enablePlayback.postValue(true)
        } else {
            mediaPlayer.Resume()
            _enablePlayback.postValue(false)
        }
    }

    fun stopPlayback() {
        mediaPlayer.Stop()
    }

    private fun getProperies(): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        val properties = this::class.declaredMemberProperties
        properties.filter { it.returnType.jvmErasure == String::class }.forEach {
            val value = it.call(this) as? String
            val displayValue = it.annotations.filterIsInstance<DisplayName>().firstOrNull()?.displayName
            if(!displayValue.isNullOrEmpty() && !value.isNullOrEmpty()){
                result.add(Pair(displayValue, value))
            }
        }
        return result
    }

    companion object {
        private const val TIMER_INITIAL_VALUE = "0:00"
        private const val TIMER_FORMAT = "%02d:%02d"
        private const val BUNDLE_TRACK_KEY = "TRACK"
    }
}