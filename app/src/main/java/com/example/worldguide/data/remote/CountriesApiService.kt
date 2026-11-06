package com.example.worldguide.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx_serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApiService {

    // Запит усіх країн
    @GET("all")
    suspend fun getAllCountries(): List<CountryDto>

    // Запит країн за обраним регіоном
    @GET("region/{region}")
    suspend fun getCountriesByRegion(@Path("region") region: String): List<CountryDto>

    // Отримання деталей країни за id (cca3 код)
    @GET("alpha/{code}")
    suspend fun getCountryByCode(@Path("code") code: String): List<CountryDto>
}

object NetworkClient {
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    private val json = Json {
        ignoreUnknownKeys = true // Ігнорувати нові/зайві поля з JSON
        coerceInputValues = true // Безпечне приведення типів
    }

    // Єдиний екземпляр Retrofit на застосунок (Singleton)
    val apiService: CountriesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CountriesApiService::class.java)
    }
}