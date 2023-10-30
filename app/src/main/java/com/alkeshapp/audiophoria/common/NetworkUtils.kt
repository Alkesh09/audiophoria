package com.alkeshapp.audiophoria.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response

suspend fun <T, R> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> Response<T>,
    mapper: ((T) -> R)? = null
): Flow<Resultat<R>> = flow {
    emit(Resultat.Loading)
    val response = apiCall.invoke()
    if (response.isSuccessful && response.body() != null) {
        val result: R = if (mapper != null) mapper(response.body()!!) else response.body() as R
        emit(Resultat.Success(result))
    } else if (response.errorBody() != null) {
        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText()).toString()
        emit(Resultat.Error(message = jsonObj))
    } else {
        emit(Resultat.Error(message = "Something went wrong!"))
    }
}.catch { e ->
    e.printStackTrace()
    emit(Resultat.Error(e.localizedMessage ?: "Something went wrong!"))
}.flowOn(dispatcher)


sealed class Resultat<out T> {
    object Loading : Resultat<Nothing>()
    class Success<T>(val data: T?) : Resultat<T>()
    class Error(val message: String) : Resultat<Nothing>()
}