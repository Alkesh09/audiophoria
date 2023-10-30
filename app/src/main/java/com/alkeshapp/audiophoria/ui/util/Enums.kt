package com.alkeshapp.audiophoria.ui.util

import androidx.annotation.StringRes
import com.alkeshapp.audiophoria.R


enum class BottomNavigationItems(val route: String, @StringRes val title: Int){
    FORYOU(
        route = "for_you",
        title = R.string.for_you
    ),
    TOPTRACKS(
        route = "top_tracks",
        title = R.string.top_tracks
    )
}