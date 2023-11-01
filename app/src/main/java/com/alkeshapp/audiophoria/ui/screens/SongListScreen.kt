package com.alkeshapp.audiophoria.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alkeshapp.audiophoria.ui.compoents.SongCard
import com.alkeshapp.audiophoria.ui.util.PlayerEvents

@Composable
fun SongListScreen(
    viewModel: SongListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    topTrack: Boolean = false,
) {
    Surface(modifier = modifier.fillMaxSize()) {
        if (viewModel.songListState.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = Color.White,
                    strokeWidth = 3.dp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(viewModel.songListState.songs.filter { song ->
                    if (topTrack) {
                        song.topTrack
                    } else {
                        true
                    }
                }) { song ->
                    SongCard(
                        song = song,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .padding(10.dp),
                        onClick = {
                            viewModel.isMiniPlayerVisible.value = true
                            viewModel.onPlayerEvents(PlayerEvents.onPlayNewSong(it))
                        }
                    )
                }
            }
        }
    }
}

