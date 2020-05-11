package com.ardritkrasniqi.prenotimi.ui.mainAcitivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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






    fun logOut() {
        viewModelScope.launch {
            val getUser = ApiService.retrofitService.logOut(token.value.toString())
            try {
                getUser.await()

            } catch (e: HttpException) {
                val error =
                    Gson().fromJson(e.response()?.errorBody()?.string(), LoginErrorResponse::class.java)
                _status.value = error.message
            }

        }
    }
}
