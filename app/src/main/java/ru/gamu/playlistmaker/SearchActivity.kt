package ru.gamu.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.features.search.TrackListAdapter
import ru.gamu.playlistmaker.features.search.network.ItunesDataQueryAsync
import ru.gamu.playlistmaker.features.search.persistance.VisitedTracksCommandHandler
import ru.gamu.playlistmaker.features.search.persistance.VisitedTracksQueryHandler
import ru.gamu.plmaker.core.Track
import ru.gamu.plmaker.core.TrackList

class SearchActivity : AppCompatActivity() {

    companion object {
        private val SEARCH_TOKEN: String = "SEARCH_TOKEN"
    }

    private var searchToken: String = ""
    private lateinit var recycler: RecyclerView
    private lateinit var tracksService: TrackList
    private var tracks: MutableList<Track> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.tbSearch)

        recycler = findViewById(R.id.trackList)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TrackListAdapter(tracks)

        //TODO: Вынести в DI контейнер
        val searchReader = ItunesDataQueryAsync(this.applicationContext)
        val historyWriter = VisitedTracksCommandHandler(this.applicationContext)
        val historyReader = VisitedTracksQueryHandler(this.applicationContext)

        tracksService = TrackList(searchReader, historyWriter, historyReader)


        if(savedInstanceState != null){
            inputEditText.setText(savedInstanceState.getString(SEARCH_TOKEN))
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(clearButton.windowToken, 0)
            visibilityWrapper(View.GONE, View.GONE)
            updateTracksAndNotify()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchToken = s.toString()
            }
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
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

    private fun updateTracksAndNotify(newTracks: List<Track> = listOf()){
        with(tracks){
            clear()
            addAll(newTracks)
            recycler.adapter?.notifyItemRangeChanged(0, size)
        }
    }

    private fun visibilityWrapper(emptyDatasetVisibility: Int, noConnectionVisibility: Int) {
        findViewById<View>(R.id.emptyDataset)?.visibility = emptyDatasetVisibility
        findViewById<View>(R.id.noConnection)?.visibility = noConnectionVisibility
    }
    private fun search(){
        val searchResult = tracksService.searchItems(searchToken)
        if(searchResult == null){
            visibilityWrapper(View.GONE, View.VISIBLE)
            updateTracksAndNotify()
        } else if(searchResult.isEmpty()) {
            visibilityWrapper(View.VISIBLE, View.GONE)
            updateTracksAndNotify()
        } else {
            visibilityWrapper(View.GONE, View.GONE)
            updateTracksAndNotify(searchResult)
        }
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