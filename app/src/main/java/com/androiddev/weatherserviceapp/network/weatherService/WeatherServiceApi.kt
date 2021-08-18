package com.androiddev.weatherserviceapp.network.weatherService

import com.androiddev.weatherserviceapp.data.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {
    @GET("current.json ")
    suspend fun getCityWeather(
        @Query("key")key:String,
        @Query("q")city:String,
        @Query("aqi")aqi:String = "no"
    ): Weather
}