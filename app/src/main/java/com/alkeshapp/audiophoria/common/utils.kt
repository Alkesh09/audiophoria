package com.alkeshapp.audiophoria.common


import androidx.media3.common.MediaItem
import com.alkeshapp.audiophoria.domain.models.Song


fun List<Song>.toMediaItemList(): MutableList<MediaItem> {
    return this.map { MediaItem.fromUri(it.url.orEmpty()) }.toMutableList()
}

