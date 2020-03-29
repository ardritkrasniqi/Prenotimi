package com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardritkrasniqi.prenotimi.model.CreateEvent
import com.ardritkrasniqi.prenotimi.model.CreatedAppointment
import com.ardritkrasniqi.prenotimi.model.LoginErrorResponse
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ShtoRezervimViewModel : ViewModel() {


    val token = MutableLiveData<String>()

    private var _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    val addAppointmentRequest = MutableLiveData<CreateEvent>()

    private val _addAppointmentResponse = MutableLiveData<CreatedAppointment>()
    val addAppointmentResponse: LiveData<CreatedAppointment>
        get() = _addAppointmentResponse


    fun addEvent() {
        viewModelScope.launch {
            val addEvent = ApiService.retrofitService.createAppointment(
                token.value.toString(), addAppointmentRequest.value
            )
            try {
                val addAppointment: CreatedAppointment = addEvent.await()
                _addAppointmentResponse.value = addAppointment
            } catch (e: HttpException) {
                val error =
                    Gson()
                        .fromJson(
                            e.response()?.errorBody()?.string(),
                            LoginErrorResponse::class.java
                        )
                            as LoginErrorResponse

                _status.value = error.message
            }
        }
    }


    fun clearStatus() {
        _status.value = ""
    }


}