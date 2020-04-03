package com.ardritkrasniqi.prenotimi.ui.mainPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.model.LoginResponse
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MainViewModel : ViewModel() {


    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status


    private var _listOfAppointments = MutableLiveData<List<Event>>()
    val listOfAppointments: LiveData<List<Event>>
        get() = _listOfAppointments


    var token = MutableLiveData<String>()


    fun getAppointments() {
        viewModelScope.launch {
            val getAppointments =
                ApiService.retrofitService.getAppointments(token.value.toString())
            try {
                val appointmentsResult = getAppointments.await()
                _listOfAppointments.value = appointmentsResult
            } catch (e: HttpException) {
                val error =
                    Gson().fromJson(
                        e.response()?.errorBody()?.string(),
                        LoginResponse::class.java
                    ) as LoginResponse

                _status.value = error.message
            }
        }
    }


}
