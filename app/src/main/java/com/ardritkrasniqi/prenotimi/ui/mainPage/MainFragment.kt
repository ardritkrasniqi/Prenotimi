package com.ardritkrasniqi.prenotimi.ui.mainPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.daysOfWeekFromLocale
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

class MainFragment : Fragment() {

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private lateinit var viewModel: MainViewModel
    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        calendarView = view.findViewById(R.id.calendarView)

        val daysOfWeek: Array<DayOfWeek> = daysOfWeekFromLocale()
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
            val layout: ConstraintLayout = view.calendarDay_layout

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = day.date
                            selectedDate = day.date
                            calendarView.notifyDateChanged(day.date)
                            oldDate.let { calendarView.notifyDateChanged(it) }
                        }
                    }
                }
            }

        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val layout = container.layout

                textView.text = day.date.dayOfMonth.toString()

                Log.i("CLICKED" , "DATE HAS BEEN CLICKED")
            }
        }




        return view
    }


//    class MonthViewContainer(view: View) : ViewContainer(view) {
//        val legendLayout = view.legendLayout
//    }
//    calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
//        override fun create(view: View) = MonthViewContainer(view)
//        override fun bind(container: MonthViewContainer, month: CalendarMonth) {
//            // Setup each header day text if we have not done that already.
//            if (container.legendLayout.tag == null) {
//                container.legendLayout.tag = month.yearMonth
//                container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
//                    tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
//                        .toUpperCase(Locale.ENGLISH)
//                    tv.setTextColorRes(R.color.example_5_text_grey)
//                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
//                }
//                month.yearMonth
//            }
//        }
//    }

}
