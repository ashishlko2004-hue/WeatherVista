package com.example.weather_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.model.WeatherResponse
import com.example.weather_app.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    object Idle : WeatherUiState
    object Loading : WeatherUiState
    data class Success(val weather: WeatherResponse) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    fun fetchWeather(city: String) {
        if (city.isBlank()) return

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val response = RetrofitInstance.api.getWeather(
                    apiKey = "0c576d6242fa4b158c370856261311",
                    city = city.trim()
                )
                _weatherData.value = response
                _uiState.value = WeatherUiState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = WeatherUiState.Error(
                    e.localizedMessage ?: "Unable to fetch weather. Please check city name or network connection."
                )
            }
        }
    }
}
