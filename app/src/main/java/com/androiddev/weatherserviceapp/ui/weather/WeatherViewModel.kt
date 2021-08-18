package com.androiddev.weatherserviceapp.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddev.weatherserviceapp.repository.WeatherRepository
import com.androiddev.weatherserviceapp.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository):
    ViewModel() {

    private val weatherStateFlow: MutableStateFlow<ApiState> =  MutableStateFlow(ApiState.Empty)
    val _postStateFlow: StateFlow<ApiState> = weatherStateFlow

    fun getCityData(search: String) = viewModelScope.launch {
        weatherStateFlow.value = ApiState.Loading
        weatherRepository.getCityWeather(search)
            .catch { e->
                weatherStateFlow.value =ApiState.Failure(e)
            }
            .collect {
                weatherStateFlow.value = ApiState.Success(it)
            }
    }
}
