package com.ardritkrasniqi.prenotimi.ui.mainAcitivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardritkrasniqi.prenotimi.model.GetUserErrorResponse
import com.ardritkrasniqi.prenotimi.model.LoginErrorResponse
import com.ardritkrasniqi.prenotimi.model.User
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivityViewModel : ViewModel() {


    val token = MutableLiveData<String>()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
      get() = _status


    val viewModelJob = Job()
    val cororutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun getUserFromServer() {
        cororutineScope.launch {
            val getUser = ApiService.retrofitService.user(token.toString())
            try {
                val userResult = getUser.await()
                _user.value = userResult

            } catch (e: HttpException) {
                val error =
                    Gson()
                        .fromJson(
                            e.response()?.errorBody()?.string(),
                            GetUserErrorResponse::class.java
                        )
                            as GetUserErrorResponse

                _status.value = error.message




            }

        }
    }
}
