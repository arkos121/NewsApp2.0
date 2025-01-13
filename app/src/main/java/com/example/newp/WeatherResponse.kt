package com.example.newp

data class WeatherResponse(
    val weather: List<WeatherDetail>,
    val main: MainWeatherData,
    val wind: WindData,
    val clouds: CloudData,
    val name: String
) {
    data class WeatherDetail(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    data class MainWeatherData(
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Int,
        val humidity: Int
    ) {
        // Utility method to convert Kelvin to Celsius
        fun tempInCelsius(k: Double): String {
            val z = k - 273.15
            return String.format("%.2f",z)
        }
    }
    data class WindData(
        val speed: Double,
        val deg: Int
    )

    data class CloudData(
        val cloud: Int
    )
}
