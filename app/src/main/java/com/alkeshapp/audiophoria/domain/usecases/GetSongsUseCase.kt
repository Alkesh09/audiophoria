package com.alkeshapp.audiophoria.domain.usecases


import android.util.Log
import com.alkeshapp.audiophoria.common.Resultat
import com.alkeshapp.audiophoria.data.mapper.toDomainSong
import com.alkeshapp.audiophoria.domain.models.Song
import com.alkeshapp.audiophoria.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(
    private val repository: SongRepository,
) {

    operator fun invoke(): Flow<Resultat<List<Song>>> = flow {
        try {
            Log.d("viewmodel", "usecase")
            emit(Resultat.Loading)
            val songs = repository.getSongsList().map { it.toDomainSong() }
            Log.d("viewmodel", "songs- ${songs}")
            emit(Resultat.Success(songs))
        } catch (e: HttpException) {
            emit(Resultat.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resultat.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
