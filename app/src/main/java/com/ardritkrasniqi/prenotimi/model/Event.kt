package com.ardritkrasniqi.prenotimi.model

import android.os.Parcel
import android.os.Parcelable
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    @Json(name = "client_name")
    val client_name: String = "",
    @Json(name = "client_phone")
    val phone: String = "",
    @Json(name = "start_date")
    val start_date: String,
    @Json(name = "end_date")
    val end_date: String,
    @Json(name = "recurring")
    val recurring: Int = 0,
    @Json(name = "recurring_frequency")
    val recurring_frequency: Int? = -1,
    @Json(name = "comment")
    val comment: String? = "",
    @Json(name = "created_at")
    val created_at: String? = null,
    @Json(name = "updated_at")
    val updated_at: String? = null,
    @Json(name = "id")
    val id: Int? = null,
    val color: String = ""

) : IEvent, Parcelable {

    fun getEventColor(): String {
        return when (this.recurring_frequency) {
            1 -> "#2E7A2E"
            2 -> "#59B545"
            3 -> "#96D151"
            else -> "#AB6574DC"
        }
    }


    override val clientName: String
        get() = client_name
    override val startTime: String
        get() = start_date.substring(10)
    override val endTime: String
        get() = end_date.substring(10)
    override val commenti: String?
        get() = comment
    override val nrTel: String?
        get() = phone

    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() as String,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

}



