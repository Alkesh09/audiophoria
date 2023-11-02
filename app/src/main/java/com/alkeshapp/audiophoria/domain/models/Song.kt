package com.alkeshapp.audiophoria.domain.models


data class Song(
    val id: Int = -1,
    val accent: String = "",
    val artist: String = "",
    val cover: String = "",
    val name: String= "",
    val topTrack: Boolean= false,
    val url: String = "",
    var isBackgroundColorEnabled: Boolean = false
)