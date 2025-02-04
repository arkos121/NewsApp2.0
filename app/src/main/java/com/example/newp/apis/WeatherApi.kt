package com.example.newp.apis

import com.example.newp.OpenWeatherMapConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi{

    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = OpenWeatherMapConfig.API_KEY
    ): WeatherResponse
}