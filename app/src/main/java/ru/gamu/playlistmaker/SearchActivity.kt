package ru.gamu.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gamu.playlistmaker.features.playlist.TrackListAdapter
import ru.gamu.playlistmaker.features.playlist.TrackQuery

class SearchActivity : AppCompatActivity() {

    companion object {
        private val SEARCH_TOKEN: String = "SEARCH_TOKEN"
    }

    private var searchToken: String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.tbSearch)

        if(savedInstanceState != null){
            inputEditText.setText(savedInstanceState.getString(SEARCH_TOKEN))
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(clearButton.windowToken, 0)
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

        val recycler = findViewById<RecyclerView>(R.id.trackList)

        val tracks = TrackQuery()

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TrackListAdapter(tracks)
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