package com.example.newp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(OpenWeatherMapConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService : WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

}