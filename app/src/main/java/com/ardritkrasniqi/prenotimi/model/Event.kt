package com.ardritkrasniqi.prenotimi.model

import com.ardritkrasniqi.prenotimi.model.data.IEvent
import com.squareup.moshi.Json



data class Event(
    @Json(name = "client_name")
    override val name: String,
    @Json(name = "client_phone")
    val phone: String,
    @Json(name = "start_date")
    val start_date: String,
    @Json(name = "end_date")
    val end_date: String,
    @Json(name = "recurring")
    val recurring: Int = 0,
    @Json(name = "comment")
    val comment: String = ""
) : IEvent {
    override val color: Int
        get() = color
    override val startTime: String
        get() = start_date
    override val endTime: String
        get() = end_date
}