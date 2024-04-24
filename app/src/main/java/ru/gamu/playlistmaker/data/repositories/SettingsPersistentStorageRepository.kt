package ru.gamu.playlistmaker.data.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

private const val PM_PREFERENCES = "PlayMakerPreference"
private const val DARKTHEME_STORAGE = "DarkThemeStorage"
class SettingsPersistentStorageRepository(context: Context) {
    private val persistentStorage: SharedPreferences
    init{
        persistentStorage = context.getSharedPreferences(
            PM_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    var useDarkTheme: Boolean
        get(){
            return persistentStorage.getBoolean(DARKTHEME_STORAGE, false)
        }
        set(value) {
            persistentStorage.edit().putBoolean(DARKTHEME_STORAGE, value).apply()
        }
}