package com.alkeshapp.audiophoria.data.mapper

import com.alkeshapp.audiophoria.data.models.SongDto
import com.alkeshapp.audiophoria.domain.models.Song

fun SongDto.toDomainSong(): Song {
    return Song(
        id = this.id ?: -1,
        accent = this.accent.orEmpty(),
        artist = this.artist.orEmpty(),
        cover = this.cover.orEmpty(),
        name = this.name.orEmpty(),
        topTrack = this.topTrack ?: false,
        url = this.url.orEmpty(),
    )
}
