package com.example.newp

data class GeoCodeResponse(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val state: String
) {
    companion object {
        // Example factory method
        fun create(
            name: String,
            latitude: Double,
            longitude: Double,
            country: String,
            state: String
        ): GeoCodeResponse = GeoCodeResponse(name, latitude, longitude, country, state)
    }

    fun getCoordinatesString(): String = "($latitude, $longitude)"
}