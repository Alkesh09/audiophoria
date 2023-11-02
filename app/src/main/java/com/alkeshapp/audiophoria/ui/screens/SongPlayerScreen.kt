package com.alkeshapp.audiophoria.ui.screens


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alkeshapp.audiophoria.R
import com.alkeshapp.audiophoria.domain.models.Song
import com.alkeshapp.audiophoria.ui.compoents.GifImage
import com.alkeshapp.audiophoria.ui.compoents.SongBigCover
import com.alkeshapp.audiophoria.ui.theme.SubTextColor
import com.alkeshapp.audiophoria.ui.util.PlayerEvents
import com.alkeshapp.audiophoria.ui.viewmodel.SongListViewModel


@Composable
fun SongPlayerScreen(
    modifier: Modifier = Modifier,
    songListViewModel: SongListViewModel = hiltViewModel(),
) {
    val song = songListViewModel.currentSongFlow.collectAsState().value

    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        SongBody(modifier = Modifier, songListViewModel, song)
    }
}

@Composable
fun SongBody(
    modifier: Modifier = Modifier,
    songListViewModel: SongListViewModel,
    song: Song
) {

    Column(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            SongBigCover(coverId = song.cover)

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = song.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight(700),
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = SubTextColor
            )
        }

        SongController(viewModel = songListViewModel)
    }

}

@Composable
fun SongController(
    viewModel: SongListViewModel,
) {

    val currentMediaPosition = viewModel.currentMediaPosition.collectAsState()
    val currentMediaDurationInMinutes = viewModel.currentSongDurationInMinutes.collectAsState()
    val currentMediaProgressInMinutes = viewModel.currentSongProgressInMinutes.collectAsState()

    var currentPos: Float = currentMediaPosition.value
    val currentPos1 = remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp, start = 25.dp, end = 25.dp, bottom = 20.dp)
    ) {

        Slider(
            value = if (currentPos1.value == 0f) currentPos else currentPos1.value,
            onValueChange = {
                Log.d("player", "seek to --> $it")
                currentPos1.value = it
            },
            onValueChangeFinished = {
                currentPos = currentPos1.value
                currentPos1.value = 0f
                viewModel.onPlayerEvents(PlayerEvents.onseekMusicDone(currentPos))
            },
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTrackColor = Color.White,
            ),
            modifier = Modifier.height(16.dp)
        )

        Row {
            Text(
                modifier = Modifier.weight(1f),
                color = Color.White,
                text = currentMediaProgressInMinutes.value,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )

            Text(
                color = Color.White,
                text = currentMediaDurationInMinutes.value,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            BuildSkipToPrevious(modifier = Modifier
                .width(35.dp)
                .height(20.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    viewModel.onPlayerEvents(PlayerEvents.onplayPreviousSong)
                })

            Spacer(modifier = Modifier.width(45.dp))

            BuildPausePlayLoadIconButtons(
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        viewModel.onPlayerEvents(PlayerEvents.onPausePlay)
                    }, viewModel
            )

            Spacer(modifier = Modifier.width(45.dp))

            BuildSkipToNext(modifier = Modifier
                .width(35.dp)
                .height(20.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    viewModel.onPlayerEvents(PlayerEvents.onplayNextSong)
                })
        }

    }
}

@Composable
fun BuildPausePlayLoadIconButtons(modifier: Modifier, viewModel: SongListViewModel) {

    val isPausePlayClicked = viewModel.isPausePlayClicked.collectAsState()
    val isPlayerBuffering = viewModel.isPlayerBuffering.collectAsState()

    if (!isPlayerBuffering.value) {
        if (isPausePlayClicked.value) {
            Image(
                modifier = modifier,
                painter = painterResource(R.drawable.play_resume),
                contentDescription = "play",
                contentScale = ContentScale.Fit,
            )
        } else {
            Image(
                modifier = modifier,
                painter = painterResource(R.drawable.play_pause),
                contentDescription = "pause",
                contentScale = ContentScale.Fit
            )
        }
    } else {
        GifImage(
            data = R.drawable.loading_big, modifier = Modifier
                .background(color = Color.Transparent)
                .clip(CircleShape)
                .size(64.dp)
        )
    }
}


@Composable
fun BuildSkipToPrevious(modifier: Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.skip_to_privious),
        contentDescription = "skip to previous song",
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(SubTextColor)
    )
}

@Composable
fun BuildSkipToNext(modifier: Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.skip_to_next),
        contentDescription = "skip to next song",
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(SubTextColor)
    )
}
