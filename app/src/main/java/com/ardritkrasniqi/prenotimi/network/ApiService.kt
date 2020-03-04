package com.ardritkrasniqi.prenotimi.network



import com.ardritkrasniqi.prenotimi.model.LoginRequest
import com.ardritkrasniqi.prenotimi.model.LoginResponse
import com.ardritkrasniqi.prenotimi.model.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST



const val BASE_URL = "http://45.76.43.73/appointment-app-backend/public/api/auth/"


val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())

    .baseUrl(BASE_URL)
    .build()


interface PrenotimiApplicationApiService {

    @POST("login")
    fun login(
        @Body loginRequest: LoginRequest?
    ): Deferred<LoginResponse>

    @GET("user/me")
    fun user(@Header("Authorization") token: String): Deferred<User>

}

object ApiService {
    val retrofitService: PrenotimiApplicationApiService by lazy {
        retrofit.create(PrenotimiApplicationApiService::class.java)
    }
}

