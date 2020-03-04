package com.ardritkrasniqi.prenotimi.ui.authUi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardritkrasniqi.prenotimi.model.LoginRequest
import com.ardritkrasniqi.prenotimi.model.LoginResponse
import com.ardritkrasniqi.prenotimi.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuthFragmentViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val loginRequest = MutableLiveData<LoginRequest>()

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    val status = MutableLiveData<String>()

    private var loginResponse = MutableLiveData<LoginResponse>()





     fun authenticate() {
        coroutineScope.launch {
            val sendUser = ApiService.retrofitService.login(loginRequest.value)
            try {
                val loginResult: LoginResponse = sendUser.await()
                status.value = "succes $loginResult"
                loginResponse.value = loginResult


            } catch (t: Throwable){
                status.value = t.message
            }

        }
    }


}