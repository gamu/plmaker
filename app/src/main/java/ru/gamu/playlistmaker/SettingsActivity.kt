package ru.gamu.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        val btnShare = findViewById<ImageView>(R.id.btnShare)
        btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            val message = getString(R.string.praktikumUrl)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, getString(R.string.optionsMessage)))
        }

        val btnSupport = findViewById<ImageView>(R.id.btnSupport)
        btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val mailTo = getString(R.string.mailTo)
            intent.data = Uri.parse("mailto:${mailTo}")
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailTheme))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailBody))
            startActivity(intent)
        }

        val btnBrowser = findViewById<ImageView>(R.id.btnBrowser)
        btnBrowser.setOnClickListener {
            val uri = Uri.parse(getString(R.string.agreementUrl))
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val editText = findViewById<EditText>(R.id.tbSearch)
        val text = editText.text.toString()
        outState.putString(SEARCH_KEY, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val editText = findViewById<EditText>(R.id.tbSearch)
        val text = savedInstanceState.getString(SEARCH_KEY, "")
        editText.setText(text)
    }

    companion object {
        val SEARCH_KEY = "searchToken"
    }
}