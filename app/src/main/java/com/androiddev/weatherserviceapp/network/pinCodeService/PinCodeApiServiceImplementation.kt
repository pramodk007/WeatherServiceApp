package com.androiddev.weatherserviceapp.network.pinCodeService

import com.androiddev.weatherserviceapp.data.PinCode
import com.androiddev.weatherserviceapp.network.pinCodeService.PinCodeApiService
import javax.inject.Inject

class PinCodeApiServiceImplementation @Inject constructor(private val pinCodeApiService: PinCodeApiService){

    suspend fun getPinCode(pinCode: String):PinCode = pinCodeApiService.getPinCode(pinCode)
}