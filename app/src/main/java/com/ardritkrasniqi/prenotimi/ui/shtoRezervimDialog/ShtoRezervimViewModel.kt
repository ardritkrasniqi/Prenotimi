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
    val appointmentId = MutableLiveData<Int>()

    private var _shtoRezerviminStatus = MutableLiveData<String>()
    val shtoRezerviminStatus: LiveData<String>
        get() = _shtoRezerviminStatus

    private var _editoRezerviminStatus = MutableLiveData<String>()
    val editoRezerviminStatus: LiveData<String>
        get() = _editoRezerviminStatus

    private var _fshijRezervmiminStatus = MutableLiveData<String>()
    val fshijRezerviminStatus: LiveData<String>
        get() = _fshijRezervmiminStatus

    val addAppointmentRequest = MutableLiveData<CreateEvent>()

    private val _addAppointmentResponse = MutableLiveData<CreatedAppointment>()
    val addAppointmentResponse: LiveData<CreatedAppointment>
        get() = _addAppointmentResponse

    private val _editedAppointmentResponse = MutableLiveData<CreatedAppointment>()
    val editedAppointmentResponse: LiveData<CreatedAppointment>
        get() = _editedAppointmentResponse




    fun addEvent() {
        viewModelScope.launch {
            val addEvent = ApiService.retrofitService.createAppointment(
                token.value.toString(), addAppointmentRequest.value
            )
            try {
                val addAppointment: CreatedAppointment = addEvent.await()
                _addAppointmentResponse.value = addAppointment
                _shtoRezerviminStatus.value = "200"


            } catch (e: HttpException) {
                val error =
                    Gson()
                        .fromJson(
                            e.response()?.errorBody()?.string(),
                            LoginErrorResponse::class.java
                        )
                            as LoginErrorResponse

                _shtoRezerviminStatus.value = error.message
            }
        }
    }


    fun editoRezervimin() {
        viewModelScope.launch {
            val editEvent = ApiService.retrofitService.editAppointment(
                appointmentId.value!!,
                token.value.toString(), addAppointmentRequest.value
            )
            try {
                val editedEvent = editEvent.await()
                _editedAppointmentResponse.value = editedEvent
                _editoRezerviminStatus.value = "200"


            } catch (e: HttpException) {
                val error =
                    Gson().fromJson(
                        e.response()?.errorBody()?.string(),
                        LoginErrorResponse::class.java
                    )
                _editoRezerviminStatus.value = error.message
            }
        }
    }


    fun deleteAppointment(id: Int) {
        viewModelScope.launch {
            val deleteAppointment = ApiService.retrofitService.deleteAppointment(
                token.value.toString(), id
            )
            try {
                deleteAppointment.await()
                _fshijRezervmiminStatus.value = "200"
            } catch (e: HttpException) {
                val error =
                    Gson().fromJson(
                        e.response()?.errorBody()?.string(),
                        LoginErrorResponse::class.java
                    )
                _fshijRezervmiminStatus.value = error.message
            }

        }
    }

}