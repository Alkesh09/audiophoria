package com.alkeshapp.audiophoria.ui.screens

import com.alkeshapp.audiophoria.domain.models.Song

data class SongListState(
    val isLoading: Boolean = false,
    val songs: List<Song> = emptyList(),
    val error: String = "",
)