package com.ardritkrasniqi.prenotimi.ui.costomViews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.decoration.Decoration
import com.ardritkrasniqi.prenotimi.decoration.IDecoration
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.ui.dayUI.DayFragment
import com.ardritkrasniqi.prenotimi.ui.dayUI.DayFragmentDirections
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarDayView : FrameLayout {
    private var dayHeight = 0
    private var eventMarginLeft = 0
    private var hourWidth = 150
    private var timeHeight = 150
    private var separateHourHeight = 0
    private var startHour = 0
    private var endHour = 24


    private var layoutDayView: LinearLayout? = null
    private var layoutEvent: FrameLayout? = null
    private var decorationn: IDecoration? = null
    private var events: List<Event>? = null


    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_day_calendar, this, true)
        layoutDayView = findViewById<View>(R.id.dayview_container) as LinearLayout
        layoutEvent = findViewById<View>(R.id.event_container) as FrameLayout
        dayHeight = resources.getDimensionPixelSize(R.dimen.dayHeight)
        if (attrs != null) {
            val a =
                context.obtainStyledAttributes(attrs, R.styleable.CalendarDayView)
            try {
                eventMarginLeft = a.getDimensionPixelSize(
                    R.styleable.CalendarDayView_eventMarginLeft,
                    eventMarginLeft
                )
                dayHeight =
                    a.getDimensionPixelSize(R.styleable.CalendarDayView_dayHeight, dayHeight)
                startHour = a.getInt(R.styleable.CalendarDayView_startHour, startHour)
                endHour = a.getInt(R.styleable.CalendarDayView_endHour, endHour)
            } finally {
                a.recycle()
            }
        }
        events = ArrayList()
        decorationn = Decoration(context)
        refresh()

    }


    private fun refresh() {
        drawDayViews()
        drawEvents()
    }

    fun make24HTime(int: Int): String{
        return if(int < 10) "0$int:00:00" else "$int:00:00"
    }


    // krijon linjat dhe oren per qdo ore te dayviewvit, merr nga item decoration
    private fun drawDayViews() {

        val navhostFragment =
            (context as AppCompatActivity).supportFragmentManager.primaryNavigationFragment as NavHostFragment
        val dayFragment = navhostFragment.childFragmentManager.primaryNavigationFragment as DayFragment

        layoutDayView!!.removeAllViews()

        var dayView: DayView? = null
        for (i in startHour..endHour) {
            dayView = decorationn?.getDayView(i)
            dayView?.setOnClickListener {
                findNavController().navigate(DayFragmentDirections.actionDayFragmentToShtoRezervimDialog(null,false,"${dayFragment.date} ${make24HTime(i)}"))
            }


            layoutDayView!!.addView(dayView)
        }
        if (dayView != null) {
            hourWidth = dayView.hourTextWidth.toInt()
            timeHeight = dayView.hourTextHeight.toInt()
            separateHourHeight = dayView.separateHeight.toInt()
        }
    }


    private fun drawEvents() {
        layoutEvent!!.removeAllViews()
        for (event in events!!) {
            val rect = getTimeBound(event)
            // add event view
            val eventView: EventView? =
                decorationn?.getEventView(event, rect, timeHeight, separateHourHeight)
            if (eventView != null) {
                layoutEvent!!.addView(eventView, eventView.layoutParams)
            }
        }
    }



    private fun getTimeBound(event: Event): Rect {
        val rect = Rect()
        rect.top =
            getPositionOfTime(timeConverterStringToCalendar(event.startTime)) + timeHeight / 2 + separateHourHeight
        rect.bottom =
            getPositionOfTime(timeConverterStringToCalendar(event.endTime)) + timeHeight / 2 + separateHourHeight
        rect.left = hourWidth + eventMarginLeft
        rect.right = width
        return rect
    }


    private fun getPositionOfTime(calendar: Calendar): Int {
        val hour = calendar[Calendar.HOUR_OF_DAY] - startHour
        val minute = calendar[Calendar.MINUTE]
        return hour * dayHeight + minute * dayHeight / 60
    }

    fun setEvents(events: List<Event>?) {
        this.events = events
        refresh()
    }

    fun setLimitTime(startHour: Int, endHour: Int) {
        require(startHour < endHour) { "start hour must before end hour" }
        this.startHour = startHour
        this.endHour = endHour
        refresh()
    }

    private fun timeConverterStringToCalendar(string: String): Calendar {
        val sdf = SimpleDateFormat("HH:mm")
        val calendar = Calendar.getInstance()
        val dateFormated = sdf.parse(string)
        calendar.time = dateFormated
        return calendar
    }

    /**
     * @param decorator decoration for draw event, popup, time
     */
    fun setDecorator(@NonNull decorator: IDecoration?) {
        decorationn = decorator
        refresh()
    }

    fun getDecoration(): IDecoration? {
        return decorationn
    }


}
