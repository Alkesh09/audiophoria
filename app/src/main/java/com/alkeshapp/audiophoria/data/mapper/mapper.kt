package com.alkeshapp.audiophoria.data.mapper

import com.alkeshapp.audiophoria.data.models.SongDto
import com.alkeshapp.audiophoria.domain.models.Song

fun SongDto.toDomainSong(): Song {
    return Song(
        id = this.id,
        accent = this.accent,
        artist = this.artist,
        cover = this.cover,
        name = this.name,
        topTrack = this.topTrack,
        url = this.url,
    )
}