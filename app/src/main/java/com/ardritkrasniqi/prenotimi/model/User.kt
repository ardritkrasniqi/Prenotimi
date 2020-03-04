package com.ardritkrasniqi.prenotimi.model

data class User(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val role_id: String,
    val company_id: String,
    val notification_token: String?,
    val active: String,
    val created_at: String?,
    val updated_at: String?
)