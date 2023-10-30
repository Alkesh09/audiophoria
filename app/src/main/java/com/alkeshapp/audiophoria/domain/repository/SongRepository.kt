package com.alkeshapp.audiophoria.domain.repository


import com.alkeshapp.audiophoria.data.models.SongDto

interface SongRepository {

    suspend fun getSongsList(): List<SongDto>
}