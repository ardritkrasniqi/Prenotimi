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
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.text.DateFormat
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


// kthen kohen nga string ne kalendar
fun timeConverterStringToCalendar(string: String): Calendar {
    val sdf = SimpleDateFormat("EEEE, MMMM dd, yyyy   HH:mm:ss")
    val calendar = Calendar.getInstance()
    val dateFormated: Date = sdf.parse(string) as Date
    calendar.time = dateFormated
    return calendar
}

// kthen kohen nga string ne local date
fun stringToLocalDate(string: String): LocalDate{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(string,formatter)
}

fun formatDateForEdits(string: String): String {
    val format = "EEEE, MMMM dd, yyyy   HH:mm"
    val simpleDateFormat: Date =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string)
    return SimpleDateFormat(format).format(simpleDateFormat)
}


fun stringToDate(stringDate: String): Date{
    val format = "yyyy-MM-dd HH:mm:ss"
    return SimpleDateFormat(format, Locale.getDefault()).parse(stringDate)
}

fun dateToString(date: Date): String{
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return dateFormat.format(date)
}

fun dateToSimpleDate(date: Date, pattern: String): String{
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return simpleDateFormat.format(date)
}



