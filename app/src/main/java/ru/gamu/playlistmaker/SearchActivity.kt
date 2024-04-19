package ru.gamu.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import ru.gamu.playlistmaker.features.search.TrackListAdapter
import ru.gamu.playlistmaker.features.search.network.ItunesDataQueryAsync
import ru.gamu.playlistmaker.features.search.persistance.VisitedTracksCommandHandler
import ru.gamu.playlistmaker.features.search.persistance.VisitedTracksQueryHandler
import ru.gamu.plmaker.core.Track
import ru.gamu.plmaker.core.TrackList
import kotlin.properties.Delegates


class SearchActivity : AppCompatActivity() {

    companion object {
        private val SEARCH_TOKEN: String = "SEARCH_TOKEN"
        private val SEARCH_DEBOUNCE_TIMEOUT_MS = 2000L
    }

    private var searchToken: String by Delegates.observable(""){ prop, oldValue, newValue ->
        if(newValue.isEmpty()){
            updateTracksAndNotify()
        }
    }

    @get:Synchronized
    @set:Synchronized
    private var searchTimeStamp: Long = System.currentTimeMillis()
    private var searchThread: Thread? = null

    private lateinit var tracksService: TrackList

    @get:Synchronized
    @set:Synchronized
    private var allowSearch: Boolean = false

    private val handler = Handler(Looper.getMainLooper())
    private var tracks: MutableList<Track> = mutableListOf()

    private val recycler: RecyclerView by lazy { findViewById(R.id.trackListRecycler) }
    private val inputEditText: EditText by lazy { findViewById(R.id.tbSearch) }
    private val clearButton: ImageView by lazy { findViewById(R.id.clearIcon) }

    @get:Synchronized
    private val searchProgressBar: LinearLayout by lazy { findViewById(R.id.searchProgress) }

    private var isShowedHistoryRresults = false
        set(value) {
            val button = findViewById<MaterialButton>(R.id.btnClearHistori)
            val searchedText = findViewById<TextView>(R.id.txtSearched)
            if(value){
                searchedText.visibility = View.VISIBLE
                button.visibility = View.VISIBLE
            }else{
                searchedText.visibility = View.GONE
                button.visibility = View.GONE
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //TODO: Вынести в DI контейнер
        val searchReader = ItunesDataQueryAsync(this.applicationContext)
        val historyWriter = VisitedTracksCommandHandler(this.applicationContext)
        val historyReader = VisitedTracksQueryHandler(this.applicationContext)

        tracksService = TrackList(searchReader, historyWriter, historyReader)

        if(!tracksService.TracksHistory.isEmpty()){
            isShowedHistoryRresults = true
            tracks = tracksService.TracksHistory.toMutableList()
        }

        val trackAdapter = TrackListAdapter(tracks)
        trackAdapter.onClickTrack = ::trackSelectHandler

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = trackAdapter

        if(savedInstanceState != null){
            inputEditText.setText(savedInstanceState.getString(SEARCH_TOKEN))
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchTimeStamp = System.currentTimeMillis()
                searchToken = s.toString()
                handler.postDelayed({
                    if(searchTimeStamp + SEARCH_DEBOUNCE_TIMEOUT_MS <= System.currentTimeMillis()){
                        search()
                    }}, SEARCH_DEBOUNCE_TIMEOUT_MS)
                allowSearch = false
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        val btmRefresh = findViewById<Button>(R.id.btnRefresh)
        btmRefresh.setOnClickListener{
            findViewById<View>(R.id.noConnection)?.visibility = View.GONE
            search()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchThread?.interrupt()
    }

    fun backButtonClick(view: View){
        finish()
    }

    fun clearHistoryClick(view: View){
        isShowedHistoryRresults = false
        tracksService.clearHistory()
        updateTracksAndNotify()
    }

    fun clearSearchBox(view: View){
        inputEditText.setText("")
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(clearButton.windowToken, 0)
        visibilityWrapper(View.GONE, View.GONE)
        if(tracksService.HistoryIsNotEmpty){
            val items = tracksService.TracksHistory.toList()
            isShowedHistoryRresults = tracksService.HistoryIsNotEmpty
            updateTracksAndNotify(items)
        }else{
            updateTracksAndNotify()
        }
    }

    private fun trackSelectHandler(track: Track){
        if(!isShowedHistoryRresults){
            tracksService.addTrackToHistory(track)
        }
        val gson = Gson()
        val intent = Intent(this, PlayerActivity::class.java)
        val bundle = Bundle()
        val serializedTrack = gson.toJson(track)
        bundle.putString("TRACK", serializedTrack)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateTracksAndNotify(newTracks: List<Track> = listOf()){
        with(tracks){
            clear()
            if(newTracks.isEmpty()){
                recycler.adapter?.notifyDataSetChanged()
            }else {
                addAll(newTracks)
                recycler.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun visibilityWrapper(emptyDatasetVisibility: Int, noConnectionVisibility: Int) {
        findViewById<View>(R.id.emptyDataset)?.visibility = emptyDatasetVisibility
        findViewById<View>(R.id.noConnection)?.visibility = noConnectionVisibility
    }
    private fun search(){
        if(searchToken.isEmpty())
            return
        val searchHandler = Handler(Looper.getMainLooper())
        Thread {
            searchHandler.post{
                searchProgressBar.visibility = View.VISIBLE
                visibilityWrapper(View.GONE, View.GONE)
            }
            searchHandler.post{ isShowedHistoryRresults = false}
            val searchResult = tracksService.searchItems(searchToken)
            if(searchResult.getIsError()){
                searchHandler.post{
                    visibilityWrapper(View.GONE, View.VISIBLE)
                    updateTracksAndNotify()
                }
            } else if(searchResult.getCount() == 0) {
                searchHandler.post {
                    visibilityWrapper(View.VISIBLE, View.GONE)
                    updateTracksAndNotify()
                }
            } else {
                searchHandler.post {
                    visibilityWrapper(View.GONE, View.GONE)
                    updateTracksAndNotify(searchResult.getResult())
                }
            }
            searchHandler.post{ searchProgressBar.visibility = View.GONE }
            searchHandler.post{ isShowedHistoryRresults = false}
        }.start()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val editText = findViewById<EditText>(R.id.tbSearch)
        val text = editText.text.toString()
        outState.putString(SEARCH_TOKEN, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val editText = findViewById<EditText>(R.id.tbSearch)
        val text = savedInstanceState.getString(SEARCH_TOKEN, "")
        editText.setText(text)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}