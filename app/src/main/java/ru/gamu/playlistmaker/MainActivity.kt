package ru.gamu.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val displayIntent = Intent(this, StubActivity::class.java)
            startActivity(displayIntent)
        }

        val btnMedia = findViewById<Button>(R.id.btnMedia)
        btnMedia.setOnClickListener {
            val displayIntent = Intent(this, StubActivity::class.java)
            startActivity(displayIntent)
        }

        val btnSettings = findViewById<Button>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}