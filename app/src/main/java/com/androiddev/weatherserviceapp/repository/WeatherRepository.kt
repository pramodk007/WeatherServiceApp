package com.androiddev.weatherserviceapp.repository

import com.androiddev.weatherserviceapp.data.PinCode
import com.androiddev.weatherserviceapp.data.Weather
import com.androiddev.weatherserviceapp.network.pinCodeService.PinCodeApiServiceImplementation
import com.androiddev.weatherserviceapp.network.weatherService.WeatherServiceApiImplementation
import com.androiddev.weatherserviceapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val pinCodeApiServiceImplementation: PinCodeApiServiceImplementation,
    private val weatherServiceApiImplementation: WeatherServiceApiImplementation
    ){

    fun getPinCode(pinCode:String): Flow<PinCode> = flow {
        emit(pinCodeApiServiceImplementation.getPinCode(pinCode))
    }.flowOn(Dispatchers.IO)

    fun getCityWeather(city:String):Flow<Weather> = flow {
        emit(weatherServiceApiImplementation.getCityWeather(Constants.KEY,city))
    }.flowOn(Dispatchers.IO)
}