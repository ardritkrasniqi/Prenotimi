package com.ardritkrasniqi.prenotimi.model


import com.squareup.moshi.Json


data class CreateEvent(
    @Json(name = "client_name")
    val client_name: String,
    @Json(name = "client_phone")
    val clientPhone: String,
    @Json(name = "start_date")
    val startDate: String,
    @Json(name = "end_date")
    val endDate: String,
    @Json(name = "recurring")
    val recurring: Int,
    @Json(name = "comment")
    val comment: String
)