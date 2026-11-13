package com.example.weather_app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 'object' means Singleton means only one object is create in entire App
object RetrofitInstance {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    val api: WeatherApi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)


    }
}
