package ru.gamu.playlistmaker.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.gamu.playlistmaker.R

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object: TypeToken<T>() {}.type)

@BindingAdapter("imageUrl")
fun setArtworkUrl(view: ImageView, url: String) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.drawable.placeholder_big)
            .into(view)
    }
}