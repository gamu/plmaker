package ru.gamu.playlistmaker.presentation.providers

import android.os.Bundle
import ru.gamu.playlistmaker.domain.models.Track
import ru.gamu.playlistmaker.utils.parseFromJson

class PlayerBundleDataProvider(val bundle: Bundle) {
    fun getData(trackKey: String): Track {
        val value = bundle.getString(trackKey)
        return parseFromJson<Track>(value!!)
    }
}