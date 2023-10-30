package com.alkeshapp.audiophoria.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alkeshapp.audiophoria.ui.compoents.SongCard

@Composable
fun SongListScreen(
    navController: NavController,
    viewModel: SongListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    topTrack: Boolean= false
) {
    Surface(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {

            items(viewModel.songListState.songs.filter { song ->
                if (topTrack) {
                    song.topTrack == true
                } else {
                    true
                }
            }) { song ->
                SongCard(
                    song = song,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}