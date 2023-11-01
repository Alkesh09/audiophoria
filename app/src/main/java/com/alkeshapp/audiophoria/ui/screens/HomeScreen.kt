package com.alkeshapp.audiophoria.ui.screens

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.alkeshapp.audiophoria.R
import com.alkeshapp.audiophoria.domain.models.Song
import com.alkeshapp.audiophoria.ui.compoents.CustomBottomNavBarItem
import com.alkeshapp.audiophoria.ui.compoents.SongPlayerView
import com.alkeshapp.audiophoria.ui.compoents.SongSmallCover
import com.alkeshapp.audiophoria.ui.theme.SmallPlayerViewBackgroud
import com.alkeshapp.audiophoria.ui.theme.TabUnselectedColor
import com.alkeshapp.audiophoria.ui.util.BottomNavigationItems
import com.alkeshapp.audiophoria.ui.util.PlayerEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val navController = rememberNavController()

    val songListViewModel: SongListViewModel = hiltViewModel()
//    val pagerState = rememberPagerState(initialPage = 0,initialPageOffsetFraction=0f, pageCount = 3)

    var currentDestination by remember {
        mutableStateOf(BottomNavigationItems.FORYOU.route)
    }


    Scaffold(bottomBar = {

        Column {
            if (songListViewModel.isMiniPlayerVisible.value) {
                SongPlayerView(songListViewModel)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
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
                        isSelected = item.route == currentDestination,
                        onClick = {
                            currentDestination = item.route
                            navController.navigate(item.route)
                        })
                }
            }
        }

    }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = currentDestination) {
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
        }
    }
}

