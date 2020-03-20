package com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardritkrasniqi.prenotimi.model.CreateEvent
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.model.LoginErrorResponse
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ShtoRezervimViewModel : ViewModel() {


    val token = MutableLiveData<String>()

    val status = MutableLiveData<String>()

    val addAppointmentRequest = MutableLiveData<CreateEvent>()

    private val _addAppointmentResponse = MutableLiveData<Event>()
    val addAppointmentResponse: LiveData<Event>
    get() = _addAppointmentResponse



//    fun addEvent(){
//        viewModelScope.launch {
//            val addEvent = ApiService.retrofitService.createAppointment(
//                //token.toString(), addAppointmentRequest.value
//            )
//
//            try {
//                val addAppointment: Event = addEvent.await()
//                _addAppointmentResponse.value = addAppointment
//            } catch (e: HttpException) {
//                val error =
//                    Gson()
//                        .fromJson(
//                            e.response()?.errorBody()?.string(),
//                            LoginErrorResponse::class.java
//                        )
//                            as LoginErrorResponse
//
//                status.value = error.message
//            }
//        }
//    }


}