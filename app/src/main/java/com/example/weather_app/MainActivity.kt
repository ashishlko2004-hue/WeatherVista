package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather_app.model.Condition
import com.example.weather_app.model.Current
import com.example.weather_app.model.Location
import com.example.weather_app.model.WeatherResponse
import com.example.weather_app.ui.theme.Weather_AppTheme
import com.example.weather_app.viewmodel.WeatherUiState
import com.example.weather_app.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Weather_AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var cityInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E3C72),
            Color(0xFF2A5298),
            Color(0xFF6DD5FA)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Weather App",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar Component
            SearchBarSection(
                cityInput = cityInput,
                onCityInputChange = { cityInput = it },
                onSearch = {
                    if (cityInput.isNotBlank()) {
                        keyboardController?.hide()
                        viewModel.fetchWeather(cityInput)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // UI State Content Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is WeatherUiState.Idle -> IdleContent()
                    is WeatherUiState.Loading -> LoadingContent()
                    is WeatherUiState.Success -> WeatherSuccessContent(weather = state.weather)
                    is WeatherUiState.Error -> ErrorContent(
                        message = state.message,
                        onRetry = {
                            if (cityInput.isNotBlank()) {
                                viewModel.fetchWeather(cityInput)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBarSection(
    cityInput: String,
    onCityInputChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = cityInput,
                onValueChange = onCityInputChange,
                placeholder = { Text("Enter city name...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color(0xFF1E3C72)
                    )
                },
                trailingIcon = {
                    if (cityInput.isNotEmpty()) {
                        IconButton(onClick = { onCityInputChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Text",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onSearch,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72)),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(text = "Search", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun IdleContent() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF1E3C72)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Search for a City",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3C72)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Type any city name above to view current temperature and weather conditions.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
            )
        }
    }
}

@Composable
fun LoadingContent() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFF1E3C72),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Fetching weather data...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E3C72)
                )
            )
        }
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error Icon",
                modifier = Modifier.size(52.dp),
                tint = Color(0xFFD32F2F)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72))
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}

@Composable
fun WeatherSuccessContent(weather: WeatherResponse) {
    val iconUrl = if (weather.current.condition.icon.startsWith("//")) {
        "https:${weather.current.condition.icon}"
    } else {
        weather.current.condition.icon
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Weather Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Location Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFF1E3C72),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = buildString {
                            append(weather.location.name)
                            weather.location.country?.let { append(", $it") }
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3C72)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weather Icon & Temperature
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = iconUrl,
                        contentDescription = weather.current.condition.text,
                        modifier = Modifier.size(110.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "${weather.current.temperature.toInt()}°C",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 64.sp,
                            color = Color(0xFF1E3C72)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Weather Condition Text
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E3C72).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = weather.current.condition.text,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E3C72)
                        )
                    )
                }

                weather.current.feelsLike?.let { feelsLike ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Feels like ${feelsLike.toInt()}°C",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Detailed Metrics Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            weather.current.humidity?.let { humidity ->
                WeatherMetricCard(
                    title = "Humidity",
                    value = "$humidity%",
                    modifier = Modifier.weight(1f)
                )
            }

            weather.current.windKph?.let { wind ->
                WeatherMetricCard(
                    title = "Wind",
                    value = "${wind.toInt()} km/h",
                    modifier = Modifier.weight(1f)
                )
            }

            weather.current.uv?.let { uv ->
                WeatherMetricCard(
                    title = "UV Index",
                    value = "$uv",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WeatherMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3C72)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherSuccessPreview() {
    Weather_AppTheme {
        WeatherSuccessContent(
            weather = WeatherResponse(
                location = Location(name = "London", country = "UK"),
                current = Current(
                    temperature = 22.0,
                    feelsLike = 21.0,
                    humidity = 65,
                    windKph = 14.5,
                    uv = 4.0,
                    condition = Condition(
                        text = "Partly Cloudy",
                        icon = "//cdn.weatherapi.com/weather/64x64/day/116.png"
                    )
                )
            )
        )
    }
}
