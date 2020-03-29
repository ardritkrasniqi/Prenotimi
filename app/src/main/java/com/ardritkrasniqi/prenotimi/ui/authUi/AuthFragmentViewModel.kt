package com.ardritkrasniqi.prenotimi.ui.authUi

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.ardritkrasniqi.prenotimi.model.LoginErrorResponse
import com.ardritkrasniqi.prenotimi.model.LoginRequest
import com.ardritkrasniqi.prenotimi.model.LoginResponse
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class AuthFragmentViewModel : ViewModel() {



    val loginRequest = MutableLiveData<LoginRequest>()

    private val _authToken = MutableLiveData<String>()
    val authToken: LiveData<String>
        get() = _authToken


    private var _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status



    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
    get() = _loginResponse




    fun authenticate() {
        viewModelScope.launch {
            val sendUser = ApiService.retrofitService.login(loginRequest.value)
            try {
                val loginResult: LoginResponse = sendUser.await()
                _status.value = loginResult.message
                _authToken.value = loginResult.data.token
                _loginResponse.value = loginResult


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

