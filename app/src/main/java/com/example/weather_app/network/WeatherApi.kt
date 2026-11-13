package com.example.weather_app.network

import com.example.weather_app.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherApi {
    @GET("current.json")
    suspend fun getWeather(
        @Query("Key") apiKey: String,
        @Query ("q") city: String
    ): WeatherResponse
}


