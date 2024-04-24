package ru.gamu.playlistmaker.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.usecases.ReadCurrentThemeUseCase
import ru.gamu.playlistmaker.usecases.UseDarkThemeUseCase

class SettingsActivity: AppCompatActivity() {
    val useDarkThemeUseCase by lazy { UseDarkThemeUseCase(this.applicationContext) }
    val readCurrentThemeUseCase by lazy { ReadCurrentThemeUseCase(this.applicationContext) }
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
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
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // Only email apps handle this.
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mailTo)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailTheme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.mailBody))
            }
            startActivity(intent)
        }

        val btnBrowser = findViewById<ImageView>(R.id.btnBrowser)
        btnBrowser.setOnClickListener {
            val uri = Uri.parse(getString(R.string.agreementUrl))
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val themeSwitch = findViewById<Switch>(R.id.swtTheme)
        themeSwitch.isChecked = readCurrentThemeUseCase.invoke()
        themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            useDarkThemeUseCase.invoke(checked)
            (applicationContext as App).switchTheme(checked)
        }
    }
}