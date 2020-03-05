package com.ardritkrasniqi.prenotimi.model

import com.squareup.moshi.Json

data class LoginErrorResponse(
    @Json(name = "message")
    val message: String? = ""
)