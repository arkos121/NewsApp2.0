package com.example.newp.utils

import com.example.newp.apis.WeatherResponse

object WeatherCache {

    private val cache = mutableMapOf<String, WeatherResponse>()

    fun get(state : String): WeatherResponse? {
        return cache[state]
    }

    fun put(state: String, weatherResponse: WeatherResponse) {
        cache[state] = weatherResponse
    }

}