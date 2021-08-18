package com.androiddev.weatherserviceapp.util

import com.androiddev.weatherserviceapp.data.Weather

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    class Success(val data: Weather) :ApiState()
    object Empty : ApiState()
}