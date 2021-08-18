package com.androiddev.weatherserviceapp.di

import com.androiddev.weatherserviceapp.network.pinCodeService.PinCodeApiService
import com.androiddev.weatherserviceapp.network.pinCodeService.PinCodeApiServiceImplementation
import com.androiddev.weatherserviceapp.network.weatherService.WeatherServiceApi
import com.androiddev.weatherserviceapp.network.weatherService.WeatherServiceApiImplementation
import com.androiddev.weatherserviceapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providePinCodeApiService():PinCodeApiService =
        Retrofit.Builder()
            .baseUrl(Constants.PIN_CODE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PinCodeApiService::class.java)


    @Singleton
    @Provides
    fun providePinCodeServiceImplementation(pinCodeApiService: PinCodeApiService) = PinCodeApiServiceImplementation(pinCodeApiService)

    @Provides
    @Singleton
    fun provideWeatherApiService():WeatherServiceApi =
        Retrofit.Builder()
            .baseUrl(Constants.WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherServiceApi::class.java)

    @Singleton
    @Provides
    fun provideWeatherServiceImplementation(weatherServiceApi: WeatherServiceApi) = WeatherServiceApiImplementation(weatherServiceApi)

}
