package com.example.worldguide.data.remote

import retrofit2.HttpException
import java.io.IOException
import kotlinx.serialization.SerializationException

sealed class NetworkError(val userMessage: String) {
    object NoInternet : NetworkError("Немає підключення до мережі. Перевірте Wi-Fi або мобільні дані.")
    object ServerError : NetworkError("Сервер тимчасово недоступний. Спробуйте пізніше.")
    object ParsingError : NetworkError("Отримано некоректну відповідь від сервера.")
    data class Unknown(val msg: String) : NetworkError(msg)
}

fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is IOException -> NetworkError.NoInternet
        is HttpException -> {
            if (code() in 500..599) NetworkError.ServerError
            else NetworkError.Unknown("Помилка сервера: код ${code()}")
        }
        is SerializationException -> NetworkError.ParsingError
        else -> NetworkError.Unknown("Виникла непередбачувана помилка: ${localizedMessage ?: "Невідомо"}")
    }
}