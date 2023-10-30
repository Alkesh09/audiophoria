package com.alkeshapp.audiophoria.data.repository



import com.alkeshapp.audiophoria.data.models.SongDto
import com.alkeshapp.audiophoria.data.service.ApiService
import com.alkeshapp.audiophoria.domain.repository.SongRepository
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(private val apiService: ApiService) : SongRepository {

    override suspend fun getSongsList(): List<SongDto> {
        return apiService.getSongsList().data
    }

}


interface SongListLocalDataSource {

}

interface SongListRemoteDataSource {

}