package com.ardritkrasniqi.prenotimi.ui.mainPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.model.LoginResponse
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import retrofit2.HttpException
import java.util.*


class MainViewModel : ViewModel() {

    // Api call variables
    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    var token = MutableLiveData<String>()

    // business logic variables

    private val _allAppointments = MutableLiveData<List<Event>>()
    val allAppointments: LiveData<List<Event>>
        get() = _allAppointments


    val currentMonth = YearMonth.now()
    val firstMonth = currentMonth.minusMonths(10)
    val lastMonth = currentMonth.plusMonths(10)
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek


    fun getAppointments() {
        viewModelScope.launch {
            val getAppointments =
                ApiService.retrofitService.getAppointments(token.value.toString())
            try {
                val appointmentsResult = getAppointments.await()
                _allAppointments.value = appointmentsResult
                Log.i("GETAPPOINTMENTS", "HELLO HELLO")
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
