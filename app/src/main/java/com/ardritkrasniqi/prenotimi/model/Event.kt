package com.ardritkrasniqi.prenotimi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
open class Event {
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1
    @ColumnInfo(name = "title")
    var title: String = ""
    @ColumnInfo(name = "start_time")
    var startTime = 0f
    @ColumnInfo(name = "end_time")
    var endTime = 0f
}