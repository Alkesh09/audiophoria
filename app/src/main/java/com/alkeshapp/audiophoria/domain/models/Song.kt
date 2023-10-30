package com.alkeshapp.audiophoria.domain.models


data class Song(
    val id: Int?,
    val accent: String?,
    val artist: String?,
    val cover: String?,
    val name: String?,
    val topTrack: Boolean?,
    val url: String?,
)