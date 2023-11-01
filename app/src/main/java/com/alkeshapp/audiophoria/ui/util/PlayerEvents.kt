package com.alkeshapp.audiophoria.ui.util

import com.alkeshapp.audiophoria.domain.models.Song


sealed class PlayerEvents {
    data class onPlayNewSong(val song: Song): PlayerEvents()
    object onPausePlay: PlayerEvents()
    object onplayPreviousSong: PlayerEvents()
    object onplayNextSong: PlayerEvents()
    data class onseekMusicDone(val value: Float): PlayerEvents()
    data class onChangeColorGradient(val url: String): PlayerEvents()
}