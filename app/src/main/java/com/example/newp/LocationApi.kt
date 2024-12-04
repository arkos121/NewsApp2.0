package com.example.newp

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName

interface LocationApi {
    @GET("geocode")
    suspend fun getGeocode(
        @Query("city") cityName: String,
        @Query("state") stateName: String,
        @Query("country") countryName: String,
        @Query("X-Api-Key") apiKey: String
    ): GeoCodeResponse

}
