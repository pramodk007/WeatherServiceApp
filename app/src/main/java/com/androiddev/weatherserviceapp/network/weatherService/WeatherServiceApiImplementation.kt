package com.androiddev.weatherserviceapp.network.weatherService

import com.androiddev.weatherserviceapp.data.Weather
import javax.inject.Inject

class WeatherServiceApiImplementation @Inject constructor(private val weatherServiceApi: WeatherServiceApi) {

    suspend fun getCityWeather(key:String,city:String): Weather = weatherServiceApi.getCityWeather(key,city)
}