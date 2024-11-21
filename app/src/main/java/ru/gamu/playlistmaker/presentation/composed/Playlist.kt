@file:OptIn(ExperimentalLayoutApi::class)

package ru.gamu.playlistmaker.presentation.composed

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.domain.models.Playlist
import ru.gamu.playlistmaker.presentation.viewmodel.playlist.PlaylistViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Playlist() {
    val darkTheme = isSystemInDarkTheme()
    val navController = LocalView.current.findNavController()
    val viewModel = viewModel(PlaylistViewModel::class.java)
    val state by viewModel.playlistsState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    Column(modifier = Modifier.padding(24.dp)){
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),){
            Button(onClick = { navController.navigate(R.id.action_mediaLibraryFragment_to_newPlayListScreen) },
                shape = RoundedCornerShape(54.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                modifier = Modifier.height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(if(darkTheme) R.color.ypWhite else R.color.ypBlack),
                    contentColor = colorResource(if(darkTheme) R.color.ypBlack else R.color.ypWhite)
                )) { Text(text="Новый плейлист",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    textAlign = TextAlign.Center
                )) }
        }
        Spacer(modifier = Modifier.height(46.dp))
        FlowRow(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceBetween,) {
            ConditionalEffect(state.items.isEmpty()){
                EmptyPlaylistsHolder()
            }
            ConditionalEffect(state.items.isNotEmpty()){
                PlaylistItemsRenderer(colorResource(if(darkTheme) R.color.ypBlack else R.color.ypWhite),
                    colorResource(if(darkTheme) R.color.ypWhite else R.color.ypBlack), state.items)
            }
        }

    }
}

@Composable
fun ConditionalEffect(condition: Boolean, effect: @Composable () -> Unit) {
    if (condition) {
        effect()
    }
}

@Composable
fun PlaylistItemsRenderer(bgColor: Color, textColor: Color,items: List<Playlist>){
    val navController = LocalView.current.findNavController()
    items.forEach {
        Box(modifier = Modifier.width(160.dp)
            .padding(bottom = 16.dp)
            .background(color = bgColor)
            .clickable {
                navController.navigate(R.id.action_mediaLibraryFragment_to_playlistEditorFragment, bundleOf("playlist" to it.playlistId))
            }) {
            Column(modifier = Modifier.padding(1.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.Start){
                PaintedImage(placeholder = painterResource(id = R.drawable.placeholder_big),
                    coverUri = Uri.parse(it.cover),
                    modifier = Modifier.height(160.dp)
                        .width(160.dp)
                        .fillMaxWidth().clip(
                        RoundedCornerShape(16.dp)))
                PlaylistLabel(textColor, title = it.title)
                PlaylistLabel(textColor, title = "${it.tracks.count()} ${it.getTrackDeclension()}")
            }
        }
    }
}

@Composable
fun PlaylistLabel(textColor: Color, title: String){
    Text(text = title,
        color = textColor,
        lineHeight = 14.sp,
        modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            textAlign = TextAlign.Start
        ))
}

@Composable
fun EmptyPlaylistsHolder(){
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()){
        Image(
            painter = painterResource(id = R.drawable.outofftracks),
            contentDescription = "Playlist",
            modifier = Modifier.height(120.dp).fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Вы не создали\nни одного плейлиста",
        color = colorResource(R.color.ypBlack),
        modifier = Modifier.fillMaxWidth(),
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 19.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            textAlign = TextAlign.Center
        ),
        maxLines = 2
    )
}

@Preview
@Composable
fun PlaylistPreview() {
    Playlist()
}