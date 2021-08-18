package com.androiddev.weatherserviceapp.network.pinCodeService

import com.androiddev.weatherserviceapp.data.PinCode
import retrofit2.http.GET
import retrofit2.http.Path

interface PinCodeApiService {

    @GET("pincode/{pinCode}")
    suspend fun getPinCode(@Path("pinCode") pinCode: String): PinCode
}