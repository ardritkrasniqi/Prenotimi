package com.ardritkrasniqi.prenotimi.model

import com.squareup.moshi.Json

data class GetUserErrorResponse(
    @Json(name = "message")
    val message: String? = "",
    @Json(name = "status_code")
    val status_code: Int?
)