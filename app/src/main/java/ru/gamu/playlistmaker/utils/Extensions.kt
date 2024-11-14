package ru.gamu.playlistmaker.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.gamu.playlistmaker.R

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object: TypeToken<T>() {}.type)

@BindingAdapter(value = ["imageUrl", "roundedCornerRadius"], requireAll = false)
fun setArtworkUrl(view: ImageView, url: String?, cornerRadius: Float?) {
    if (!url.isNullOrEmpty()) {
        val requestOptions = RequestOptions().apply {
            cornerRadius?.let{
                transform(RoundedCorners(it.toInt()))
            }
        }
        Glide.with(view.context)
            .load(url)
            .apply(requestOptions)
            .placeholder(R.drawable.placeholder_big)
            .into(view)
    } else {
        view.setImageResource(R.drawable.placeholder_big)
    }
}