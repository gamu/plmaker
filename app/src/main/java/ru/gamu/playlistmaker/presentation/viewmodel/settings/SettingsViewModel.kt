package ru.gamu.playlistmaker.presentation.viewmodel.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ru.gamu.playlistmaker.App
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.domain.usecases.CurrentThemeInteractor
import ru.gamu.playlistmaker.domain.usecases.DarkThemeInteractor

class SettingsViewModel(val darkThemeInteractor: DarkThemeInteractor,
                        val currentThemeInteractor: CurrentThemeInteractor,
                        val app: Context) : ViewModel() {

    private val darkThemeLiveData = MutableLiveData<Boolean>()

    var theme: Boolean = false
        set(value) {
            darkThemeLiveData.value = value
            field = value
        }
        get(){ return darkThemeLiveData.value!! }

    init{
        darkThemeLiveData.value = currentThemeInteractor.invoke()
    }

    fun addThelmeChangeListener(owner: LifecycleOwner, observer: Observer<Boolean>){
        darkThemeLiveData.observe(owner, observer)
    }

    fun toggleTheme(darkTheme: Boolean){
        val currentTheme = currentThemeInteractor.invoke()
        if(darkTheme != currentTheme){
            darkThemeInteractor.invoke(darkTheme)
            (app as App).switchTheme(darkTheme)
        }
    }

    fun showAgreement(action: (intent: Intent) -> Unit){
        val uri = Uri.parse(app.getString(R.string.agreementUrl))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        action.invoke(intent)
    }

    fun shareApplication(action: (intent: Intent) -> Unit){
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        val message = app.getString(R.string.praktikumUrl)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        action.invoke(intent)
    }

    fun sendMail(action: (intent: Intent) -> Unit){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps handle this.
            putExtra(Intent.EXTRA_EMAIL, arrayOf(app.getString(R.string.mailTo)))
            putExtra(Intent.EXTRA_SUBJECT, app.getString(R.string.mailTheme))
            putExtra(Intent.EXTRA_TEXT, app.getString(R.string.mailBody))
        }
        action(intent)
    }
}