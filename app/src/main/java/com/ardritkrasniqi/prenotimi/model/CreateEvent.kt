package com.ardritkrasniqi.prenotimi.model


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class CreateEvent(
    @Json(name = "client_name")
    val clientName: String,
    @Json(name = "client_phone")
    val clientPhone: String,
    @SerializedName("start_date")
    val startDate: String,
    @Json(name = "end_date")
    val endDate: String,
    val recurring: Int,
    val comment: String
)