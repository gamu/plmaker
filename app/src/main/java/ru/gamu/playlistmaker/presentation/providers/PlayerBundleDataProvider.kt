package ru.gamu.playlistmaker.presentation.providers

import android.os.Bundle
import ru.gamu.playlistmaker.presentation.models.TrackInfo
import ru.gamu.playlistmaker.utils.parseFromJson

class PlayerBundleDataProvider(val bundle: Bundle) {
    fun getData(trackKey: String): TrackInfo {
        val value = bundle?.getString(trackKey)
        return parseFromJson<TrackInfo>(value!!)
    }
}