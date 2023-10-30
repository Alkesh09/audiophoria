package com.alkeshapp.audiophoria.data.service

import com.alkeshapp.audiophoria.data.models.GetSongsListResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("items/songs")
    suspend fun getSongsList(): GetSongsListResponse


    companion object {
        const val BASE_URL = "https://cms.samespace.com/"
    }
}