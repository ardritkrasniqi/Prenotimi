package com.ardritkrasniqi.prenotimi.model

import com.squareup.moshi.Json


data class LoginResponse(
    @Json(name = "data")
    val data: Data,
    @Json(name = "message")
    val message: String
)