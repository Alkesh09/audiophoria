package com.alkeshapp.audiophoria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alkeshapp.audiophoria.ui.compoents.CustomBottomNavBarItem
import com.alkeshapp.audiophoria.ui.compoents.SongPlayerView
import com.alkeshapp.audiophoria.ui.util.BottomNavigationItems
import com.alkeshapp.audiophoria.ui.util.Constants
import com.alkeshapp.audiophoria.ui.viewmodel.SongListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val navController = rememberNavController()

    val songListViewModel: SongListViewModel = hiltViewModel()
//    val pagerState = rememberPagerState(initialPage = 0,initialPageOffsetFraction=0f, pageCount = 3)


    Scaffold(bottomBar = {
        Column {
            if (!songListViewModel.isSheetOpen.value) {
                if (songListViewModel.isMiniPlayerVisible.value) {
                    SongPlayerView(
                        songListViewModel,
                        onClick = {
                            songListViewModel.isSheetOpen.value = true
                        })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF150F07).copy(alpha = 1f),
                                    Color.Black
                                )
                            )
                        )
                        .padding(top = 10.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    BottomNavigationItems.values().forEach { item ->
                        CustomBottomNavBarItem(
                            bottomNavItem = item,
                            isSelected = item.route == songListViewModel.currentDestination.value,
                            onClick = {
                                songListViewModel.currentDestination.value = item.route
                                navController.navigate(item.route)
                            })
                    }
                }
            }
        }
    }) { paddingValues ->


        Surface(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = songListViewModel.currentDestination.value
            ) {
                composable(BottomNavigationItems.FORYOU.route) {
                    SongListScreen(
                        viewModel = songListViewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 15.dp, horizontal = 20.dp)
                    )
                }
                composable(BottomNavigationItems.TOPTRACKS.route) {
                    SongListScreen(
                        viewModel = songListViewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 15.dp, horizontal = 20.dp),
                        topTrack = true
                    )
                }
            }

            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded= true)

            if (songListViewModel.isSheetOpen.value) {
                ModalBottomSheet(
                    onDismissRequest = { songListViewModel.isSheetOpen.value = false },
                    sheetState = sheetState,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        SongPlayerScreen(
                            songListViewModel = songListViewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                }
            }

        }
    }
}

