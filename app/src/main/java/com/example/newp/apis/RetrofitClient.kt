package com.example.newp.apis

import com.example.newp.OpenWeatherMapConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(OpenWeatherMapConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService : WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

}