package com.alkeshapp.audiophoria.ui.compoents

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.alkeshapp.audiophoria.R
import com.alkeshapp.audiophoria.ui.viewmodel.SongListViewModel
import com.alkeshapp.audiophoria.ui.theme.SmallPlayerViewBackgroud
import com.alkeshapp.audiophoria.ui.util.PlayerEvents


@Composable
fun SongPlayerView(
    songListViewModel: SongListViewModel,
    onClick: () -> Unit,
) {
    val song = songListViewModel.currentSongFlow.collectAsState().value
    val isPlayerBuffering = songListViewModel.isPlayerBuffering.collectAsState().value
    val pausePlay = songListViewModel.isPausePlayClicked.collectAsState().value

    Box(
        modifier = Modifier
            .height(64.dp)
            .background(color = SmallPlayerViewBackgroud)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 33.dp, end = 20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SongSmallCover(coverId = song.cover)

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = song.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }


            if (!isPlayerBuffering) {
                Image(
                    painter = painterResource(id = if (pausePlay) R.drawable.play_resume else R.drawable.play_pause),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clip(CircleShape)
                        .size(35.dp)
                        .clickable {
                            songListViewModel.onPlayerEvents(PlayerEvents.onPausePlay)
                        }
                )
            } else {
                GifImage(
                    data = R.drawable.loading, modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(end = 16.dp)
                        .clip(CircleShape)
                        .size(35.dp)
                )
            }


        }
    }
}

@Composable
fun GifImage(data: Any?, modifier: Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = data).apply(
                block = { size(Size.ORIGINAL) }
            )
                .build(),
            imageLoader = imageLoader,
        ),
        contentDescription = null,
        modifier = modifier
    )
}