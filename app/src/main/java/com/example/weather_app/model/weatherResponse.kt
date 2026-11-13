package com.example.weather_app.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val country: String? = null,
    val region: String? = null
)

data class Current(

    @SerializedName("temp_c")
    val temperature: Double,
    @SerializedName("feelslike_c")
    val feelsLike: Double? = null,
    val humidity: Int? = null,
    @SerializedName("wind_kph")
    val windKph: Double? = null,
    val uv: Double? = null,
    val condition: Condition
)
data class Condition(
    val text: String,
    val icon: String
)
