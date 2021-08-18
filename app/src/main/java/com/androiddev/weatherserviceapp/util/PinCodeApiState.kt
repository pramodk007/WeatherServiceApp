package com.androiddev.weatherserviceapp.util

import com.androiddev.weatherserviceapp.data.PinCode

sealed class PinCodeApiState {
    object Loading : PinCodeApiState()
    class Failure(val msg:Throwable) : PinCodeApiState()
    class Success(val data:PinCode) :PinCodeApiState()
    object Empty : PinCodeApiState()
}