package ru.gamu.playlistmaker.presentation.composed

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun PaintedImage(placeholder: Painter, coverUri: Uri, modifier: Modifier = Modifier) {
    val resolver = LocalContext.current.contentResolver
    var imgPainter = placeholder
    if(coverUri.toString().isNotEmpty()) {
        val stream = resolver.openInputStream(coverUri)
        val drawable = Drawable.createFromStream(stream, coverUri.toString())
        imgPainter = rememberDrawablePainter(drawable)
    }
    Image(
        painter = imgPainter,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
    )
}