package com.ardritkrasniqi.prenotimi.ui.authUi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardritkrasniqi.prenotimi.model.LoginErrorResponse
import com.ardritkrasniqi.prenotimi.model.LoginRequest
import com.ardritkrasniqi.prenotimi.model.LoginResponse
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class AuthFragmentViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineContext: CoroutineContext get() = viewModelJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    val loginRequest = MutableLiveData<LoginRequest>()

//    private val _authToken = MutableLiveData<String>()
//    val authToken: LiveData<String>
//        get() = _authToken


    private var _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status


    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
    get() = _loginResponse


    fun authenticate() {
        scope.launch {
            val sendUser = ApiService.retrofitService.login(loginRequest.value)
            try {
                val loginResult: LoginResponse = sendUser.await()


                _status.value = "succes ${loginResult.message}"
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        coroutineContext.cancel()
        scope.cancel()
    }


}

