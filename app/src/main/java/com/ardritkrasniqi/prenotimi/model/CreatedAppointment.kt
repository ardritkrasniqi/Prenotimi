package com.ardritkrasniqi.prenotimi.model


import com.squareup.moshi.Json


data class CreatedAppointment(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "company_id")
    val companyId: String? = null,
    @Json(name = "user_id")
    val userId: Int? = null,
    @Json(name = "appointment_type_id")
    val appointment_type_id: Int? = null,
    @Json(name = "client_name")
    val clientName: String? = null,
    @Json(name = "client_phone")
    val clientPhone: String? = null,
    @Json(name = "start_date")
    val startDate: String? = null,
    @Json(name = "end_date")
    val endDate: String? = null,
    val recurring: Int? = null,
    @Json(name = "recurring_frequency")
    val recurringFreq: Int? = null,
    val comment: String? = null,
    @Json(name = "created_at")
    val createdAt: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String? = null
)