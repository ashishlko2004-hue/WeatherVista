# 🌤️ WeatherVista

**WeatherVista** is a modern, responsive Android application built with **Jetpack Compose**, **Retrofit**, and **Kotlin Coroutines**. It provides real-time weather forecasts and atmospheric insights for any city around the world.

---

## ✨ Features

- 🔍 **Global City Search**: Get live weather data for any location worldwide.
- 🌡️ **Temperature & Feels Like**: View current temperatures alongside realistic "Feels Like" metrics.
- 💧 **Detailed Weather Metrics**: Track Humidity (`%`), Wind Speed (`km/h`), and UV Index at a glance.
- 🎨 **Modern Compose UI**: Elegant gradient card interfaces, custom typography, and responsive layouts.
- 🖼️ **Dynamic Icons**: High-resolution condition icons loaded via **Coil**.
- ⚡ **UI State Management**: Sealed `WeatherUiState` managing Idle, Loading, Success, and Error states with retry logic.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Networking**: Retrofit 2 + Gson Converter
- **Async & Concurrency**: Kotlin Coroutines & StateFlow
- **Image Loading**: Coil Compose
- **Architecture**: MVVM (Model-View-ViewModel)

---

## 🚀 Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/ashishlko2004-hue/WeatherVista.git
   ```
2. Open the project in **Android Studio**.
3. Build and run on an emulator or physical device (Min SDK: 24, Target SDK: 37).
