package com.ardritkrasniqi.prenotimi.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.ardritkrasniqi.prenotimi.dpToPx
import java.util.*
import java.util.concurrent.TimeUnit




    public fun generateCalendarWidth(context: Context): Int {
        val dm = DisplayMetrics()
        val windowManager: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(dm)
        val eventLayoutWidth = (dm.widthPixels - dpToPx(100, context))

        var calendarWidth = eventLayoutWidth / 3
        calendarWidth = (calendarWidth - (dpToPx(20, context)))

        return calendarWidth
    }

    public fun screenWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.widthPixels
    }


    public fun getEventTimeFrame(start: Date, end: Date): Long {
        var end = end
        if (end.time - start.time < 0) {
            val c = Calendar.getInstance()
            c.time = end
            c.add(Calendar.DATE, 1)
            end = c.time
        }
        val ms = end.time - start.time
        return TimeUnit.MINUTES.convert(
            ms,
            TimeUnit.MILLISECONDS
        )
    }





