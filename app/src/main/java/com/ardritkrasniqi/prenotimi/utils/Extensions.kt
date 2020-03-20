package com.ardritkrasniqi.prenotimi.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.ardritkrasniqi.prenotimi.model.Event
import com.kizitonwose.calendarview.model.CalendarDay
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.text.SimpleDateFormat
import java.util.*

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}


internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

public fun dpToPx(dp: Int, context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()

fun screenDimensions(heightOrWidth: String): Int {
    val displayMetrics: DisplayMetrics = DisplayMetrics()

    return if (heightOrWidth == "height") displayMetrics.heightPixels else displayMetrics.widthPixels
}

fun CalendarDay.addEvents(events: MutableList<Event>) {
    this.events = events
}

var CalendarDay.events: MutableList<Event>
    get() = mutableListOf()
    set(value) = addEvents(value)


fun timeConverterStringToCalendar(string: String): Calendar {
    val sdf = SimpleDateFormat("EEEE, MMMM dd, yyyy   HH:mm:ss")
    val calendar = Calendar.getInstance()
    val dateFormated = sdf.parse(string)
    calendar.time = dateFormated
    return calendar
}


