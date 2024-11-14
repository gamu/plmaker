package ru.gamu.playlistmaker.presentation.composed

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.presentation.viewmodel.playlist.NewPlaylistViewModel


@Composable
fun NewPlaylist(){
    val darkTheme = isSystemInDarkTheme()
    val navController = LocalView.current.findNavController()
    val viewModel = viewModel<NewPlaylistViewModel>()
    val state by viewModel.titleState.collectAsState()
    val ctx = LocalContext.current

    val contentColor = colorResource(if(darkTheme) R.color.ypWhite else R.color.ypBlack)

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.setCover(uri)
        }
    }

    Column(verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(16.dp)){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
                horizontalArrangement = Arrangement.Start) {
                    Image(painter = painterResource(if(darkTheme) R.drawable.arrow_back_dark else R.drawable.arrow_back_light),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() })
                Text(stringResource(R.string.createPlaylistHeader),
                    color = colorResource(if(darkTheme) R.color.ypWhite else R.color.ypBlack),
                    modifier = Modifier.padding(start = 24.dp),
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_bold)),
                        textAlign = TextAlign.Start))
            }
            Box(modifier = Modifier
                .height(310.dp)
                .width(310.dp)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                contentAlignment = Alignment.Center,
            ) {
                PaintedImage(painterResource(R.drawable.add_playlist_bg),
                    state.cover,
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)))
            }
            Spacer(modifier = Modifier.size(32.dp))
            PlaymakerOutlinedEdit(state.title, stringResource(R.string.playlisNamePlaceholder), contentColor) {
                viewModel.setTitle(it)
            }
            Spacer(modifier = Modifier.size(16.dp))
            PlaymakerOutlinedEdit(state.description, stringResource(R.string.playlistDescriptionPlaceholder), contentColor) {
                viewModel.setDescription(it)
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
        val appContext = LocalContext.current.applicationContext
        Button(onClick = {
            viewModel.SavePlaylis(ctx)
            navController.popBackStack()
            Toast.makeText(appContext, "Плэйлист создан", Toast.LENGTH_SHORT).show() },
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            enabled = state.isComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (state.isComplete) colorResource(R.color.ypBlue) else colorResource(R.color.newPlayListButton)
            )){
            Text(stringResource(R.string.createPlaylistLabel), color = colorResource(R.color.ypWhite))
        }
    }
}

@Composable
fun PlaymakerOutlinedEdit(value: String, placeholder: String, contentColor: Color, block: (String) -> Unit) {
    val borderColor = if (value.isNotEmpty()) colorResource(R.color.ypBlue) else colorResource(R.color.ypGray)
    val labelColor = if (value.isNotEmpty()) colorResource(R.color.ypBlue) else colorResource(R.color.ypBlack)
    val valueInternalState = remember { mutableStateOf(true) }
    OutlinedTextField(
        value = value,
        label = { Text(placeholder, color = if(valueInternalState.value) contentColor else colorResource(R.color.ypBlue)) },
        onValueChange = { it ->
            valueInternalState.value = it.isEmpty()
            block.invoke(it) },
        placeholder = { Text(placeholder, color = contentColor) },
        modifier = Modifier
            .padding()
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = contentColor,
            cursorColor = colorResource(R.color.ypBlue),
            focusedLabelColor = colorResource(R.color.ypBlue),
            unfocusedLabelColor = labelColor,
            focusedBorderColor = colorResource(R.color.ypBlue),
            unfocusedBorderColor = borderColor,
        )

    )
}

@Preview
@Composable
fun NewPlaylistPreview() {
    NewPlaylist()
}