package com.ardritkrasniqi.prenotimi.ui.mainPage

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.utils.addEvents
import com.ardritkrasniqi.prenotimi.utils.daysOfWeekFromLocale
import com.ardritkrasniqi.prenotimi.utils.events
import com.ardritkrasniqi.prenotimi.utils.setTextColorRes
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.calendar_days_header.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

class MainFragment : Fragment() {

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private lateinit var viewModel: MainViewModel
    private lateinit var calendarView: CalendarView
    private val today = LocalDate.now()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        calendarView = view.findViewById(R.id.calendarView)


        val daysOfWeek: Array<DayOfWeek> =
            daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()


        calendarView.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            daysOfWeek.first()
        )
        calendarView.scrollToMonth(currentMonth)


        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            var textView: TextView = view.calendarDay_text
            var layout: ConstraintLayout = view.calendarDay_layout
            val sotIndicator: ImageView = view.sot_indicator

            init {
                view.setOnClickListener {
                    day.events // TODO qoji kto evente ne DAYView
                    NavHostFragment.findNavController(this@MainFragment).navigate(R.id.dayFragment)
                }
//                view.setOnClickListener {
//                    if (day.owner == DayOwner.THIS_MONTH) {
//                        if (selectedDate != day.date) {
//                            val oldDate = day.date
//                            selectedDate = day.date
//                            calendarView.notifyDateChanged(day.date)
//                            oldDate.let { calendarView.notifyDateChanged(it) }
//
//
//                        }
//                    }
//                }
            }
        }


        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                //day.addEvents(viewModel.events.filter { it.start_date == day.date.format() }.toMutableList())

                val textView = container.textView
                val layout = container.layout
                val sotIndicator = container.sotIndicator

//                val itemview1 = container.view1
//                itemview1 = day.find()
//                //TODO plotesoji vijat ne VIEW me data te eventeve

                textView.text = day.date.dayOfMonth.toString()


                if (day.owner == DayOwner.THIS_MONTH) {
                    when (day.date) {
                        today -> {
                            layout.setBackgroundResource(R.drawable.today_date_background)
                            sotIndicator.visibility = View.VISIBLE
                        }
//                        selectedDate -> {
//                            layout.setBackgroundResource(R.drawable.selected_bg)
//
//                        }
                        else -> {
                            layout.background = null
                        }
                    }
//                    textView.setTextColorRes(R.color.toolbar_color)
//                    layout.setBackgroundResource(if (day.date == selectedDate) R.drawable.selected_bg else 0)

                } else {
                    // per datat te cilat nuk i perkasin muajit i cili eshte i paraqitur
                    textView.setTextColorRes(R.color.white)
                    layout.background = null

                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = view.legendLayout
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text =
                                daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                                    .toUpperCase(Locale.ENGLISH)
                            tv.setTextColorRes(R.color.white)
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                        }

                    month.yearMonth
                }
            }
        }
        calendarView.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            //monthYear_text.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                calendarView.notifyDateChanged(it)

            }
        }
        return view
    }
}
