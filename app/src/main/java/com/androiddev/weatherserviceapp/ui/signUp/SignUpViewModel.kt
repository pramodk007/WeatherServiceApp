package com.androiddev.weatherserviceapp.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddev.weatherserviceapp.repository.WeatherRepository
import com.androiddev.weatherserviceapp.util.PinCodeApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SignUpViewModel @Inject constructor(private val weatherRepository: WeatherRepository):ViewModel(){

    private val pinCodeStateFlow:MutableStateFlow<PinCodeApiState> =  MutableStateFlow(PinCodeApiState.Empty)
    val _pinCodeStateFlow: StateFlow<PinCodeApiState> = pinCodeStateFlow

    fun getPinCode(pinCode:String) = viewModelScope.launch {
        pinCodeStateFlow.value = PinCodeApiState.Loading
        weatherRepository.getPinCode(pinCode)
            .catch { e ->
                pinCodeStateFlow.value = PinCodeApiState.Failure(e)
            }
            .collect {
                pinCodeStateFlow.value = PinCodeApiState.Success(it)
            }
    }
}