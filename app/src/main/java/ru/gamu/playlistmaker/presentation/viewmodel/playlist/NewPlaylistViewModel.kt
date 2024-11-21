package ru.gamu.playlistmaker.presentation.viewmodel.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import ru.gamu.playlistmaker.domain.usecases.CreatePlaylistInteractor
import java.io.File
import java.io.FileOutputStream

@Parcelize
data class PlaylistInfoState(
    val title: String = "",
    val description: String = "",
    val cover: Uri = Uri.EMPTY,
    val isComplete: Boolean = false,
    val hasCover: Boolean = false
) : Parcelable

class NewPlaylistViewModel(private val savedStateHandler: SavedStateHandle): ViewModel() {
    private val createPlaylistInteractor: CreatePlaylistInteractor by inject(CreatePlaylistInteractor::class.java)

    val titleState = savedStateHandler.getStateFlow(STATE_KEY, PlaylistInfoState())

    private fun saveCover(ctx: Context, uri: Uri): Uri {
        val imgCover =
            File(ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image_cover")

        if (!imgCover.exists()) {
            imgCover.mkdir()
        }
        val currentTime = System.currentTimeMillis()
        val imageFile = File(imgCover, "$currentTime.jpg")
        val inputStream = ctx.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(imageFile)
        val uriImgStorage = Uri.fromFile(imageFile)

        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return uriImgStorage
    }

    fun SavePlaylis(ctx: Context) {
        viewModelScope.launch {
            var cover = titleState.value.cover.toString()
            if(cover.isNotEmpty()) {
                cover = saveCover(ctx, titleState.value.cover).toString()
            }
            createPlaylistInteractor.invoke(titleState.value.title,
                titleState.value.description, cover, null)
        }
    }

    fun setCover(coverUri: Uri) {
        savedStateHandler[STATE_KEY] = titleState.value.copy(cover = coverUri, hasCover = true)
    }

    fun setTitle(title: String) {
        val isComplete = title.isNotEmpty()
        savedStateHandler[STATE_KEY] = titleState.value.copy(title = title, isComplete = isComplete)
    }

    fun setDescription(description: String) {
        savedStateHandler[STATE_KEY] = titleState.value.copy(description = description, isComplete = true)
    }

    companion object {
        const val STATE_KEY  = "state"
    }
}