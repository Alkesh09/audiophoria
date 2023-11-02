package com.alkeshapp.audiophoria.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.alkeshapp.audiophoria.data.repository.SongRepositoryImpl
import com.alkeshapp.audiophoria.data.service.ApiService
import com.alkeshapp.audiophoria.domain.repository.SongRepository
import com.alkeshapp.audiophoria.player.MusicPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ApiModule {



    @Singleton
    @Provides
    fun provideSongRepository(apiService: ApiService): SongRepository = SongRepositoryImpl(apiService)

    @Singleton
    @Provides
    fun provideRetrofitClient(): ApiService {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}