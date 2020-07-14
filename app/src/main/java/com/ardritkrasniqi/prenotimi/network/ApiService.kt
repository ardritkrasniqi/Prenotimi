package com.ardritkrasniqi.prenotimi.network


import com.ardritkrasniqi.prenotimi.BuildConfig
import com.ardritkrasniqi.prenotimi.model.*

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


const val BASE_URL = BuildConfig.BASE_URL


val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())

    .baseUrl(BASE_URL)
    .build()


interface PrenotimiApplicationApiService {

    //login
    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest?
    ): Deferred<LoginResponse>

    //Refresh token
    @PATCH("auth/refresh")
    fun refreshToken(@Header("Authorization") token: String)

    //User profile
    @GET("auth/user/me")
    fun user(
        @Header("Authorization") token: String
    ): Deferred<User>

    //Create CreatedAppointment
    @POST("appointment/create")
    fun createAppointment(
        @Header("Authorization") token: String,
        @Body createEvent: CreateEvent?
    ): Deferred<CreatedAppointment>

    // All Appointments
    @GET("appointment/all")
    fun getAppointments(@Header("Authorization") token: String): Deferred<List<Event>>

    // Edit Appointment
    @POST("appointment/edit/{id}")
    fun editAppointment(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body createEvent: CreateEvent?
    ): Deferred<CreatedAppointment>

    // Delete appointment
    @DELETE("appointment/delete/{id}")
    fun deleteAppointment(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Deferred<Unit>

    //Log out
    @DELETE("api/auth/invalidate")
    fun logOut(
        @Header("Authorization") token: String
    ):Deferred<Unit>

}

object ApiService {
    val retrofitService: PrenotimiApplicationApiService by lazy {
        retrofit.create(PrenotimiApplicationApiService::class.java)
    }
}