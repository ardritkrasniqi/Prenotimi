package com.ardritkrasniqi.prenotimi.ui.mainPage

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.ardritkrasniqi.prenotimi.utils.daysOfWeekFromLocale
import com.ardritkrasniqi.prenotimi.utils.setTextColorRes
import com.ardritkrasniqi.prenotimi.utils.stringToLocalDate
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
import java.io.Serializable
import java.util.*

class MainFragment : Fragment(), Serializable {

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private lateinit var allAppointments: MutableList<Event>

    private lateinit var viewModel: MainViewModel
    private lateinit var calendarView: CalendarView
    private val today = LocalDate.now()
    val ditetEJaves = arrayOf("Dielë", "Hënë", "Martë", "Mërkurë", "Enjte", "Premte", "Shtunë")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val sharedPreferences = PreferenceProvider(this.requireContext())
        viewModel.token.value = "Bearer ${sharedPreferences.getToken()}"
        viewModel.getAppointments()
        calendarView = view.findViewById(R.id.calendarView)


        val daysOfWeek: Array<DayOfWeek> =
            daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()




        allAppointments = mutableListOf()
        viewModel.listOfAppointments.observe(viewLifecycleOwner, Observer { list ->
            allAppointments = list as MutableList<Event>
        })




        calendarView.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            daysOfWeek.first()
        )
        calendarView.scrollToMonth(currentMonth)


            class DayViewContainer(view: View) : ViewContainer(view), Serializable {
                lateinit var day: CalendarDay
                var textView: TextView = view.calendarDay_text
                var layout: ConstraintLayout = view.calendarDay_layout
                val sotIndicator: ImageView = view.sot_indicator
                val eventItem1 = view.event_item_1
                val eventItem2 = view.event_item_2
                val eventItem3 = view.event_item_3
                val moreEvents = view.more_appointments_dots

                val bundle = Bundle()

                // klikimi mbi qdo qeli te kalendarit
                init {

                    view.setOnClickListener {
                        val dayList = allAppointments.filter {
                            stringToLocalDate(it.start_date.substring(0, 10)) == (day.date)
                        }
                        Log.i("QELIA", day.date.toString())
                        bundle.putParcelableArrayList(
                            "rezervimet",
                            dayList as ArrayList<out Parcelable>
                        )
                        view.findNavController()
                            .navigate(R.id.action_mainFragment2_to_dayFragment, bundle)
                    }
                }
            }


            // krijimi e diteve te kalendarit

            calendarView.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    var eventItemCounter = 0
                    val eventList = mutableListOf<Event>()
                    val res = resources

                    for (i in allAppointments) {
                        if (stringToLocalDate(i.start_date.substring(0, 10)) == (day.date)) {
                            eventItemCounter++
                            eventList.add(i)
                        }
                    }

                    //Log.i("DAYLIST", day.events.toString())
                    val textView = container.textView
                    val layout = container.layout
                    val sotIndicator = container.sotIndicator
                    val eventItem1 = container.eventItem1
                    val eventItem2 = container.eventItem2
                    val eventItem3 = container.eventItem3
                    val moreEvents = container.moreEvents

                    if (day.owner == DayOwner.THIS_MONTH) {
                        when (eventItemCounter) {
                            1 -> {
                                eventItem1.visibility = View.VISIBLE
                                eventItem1.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[0].start_date.substring(10, 16),
                                    eventList[0].client_name.toUpperCase()
                                )
                            }
                            2 -> {
                                eventItem1.visibility = View.VISIBLE
                                eventItem1.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[0].start_date.substring(10, 16),
                                    eventList[0].client_name.toUpperCase()
                                )
                                eventItem2.visibility = View.VISIBLE
                                eventItem2.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[1].start_date.substring(10, 16),
                                    eventList[1].client_name.toUpperCase()
                                )
                            }
                            3 -> {
                                eventItem1.visibility = View.VISIBLE
                                eventItem1.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[0].start_date.substring(10, 16),
                                    eventList[0].client_name.toUpperCase()
                                )
                                eventItem2.visibility = View.VISIBLE
                                eventItem2.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[1].start_date.substring(10, 16),
                                    eventList[1].client_name.toUpperCase()
                                )
                                eventItem3.visibility = View.VISIBLE
                                eventItem3.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[2].start_date.substring(10, 16),
                                    eventList[2].client_name.toUpperCase()
                                )
                            }
                            4 -> {
                                eventItem1.visibility = View.VISIBLE
                                eventItem1.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[0].start_date.substring(10, 16),
                                    eventList[0].client_name.toUpperCase()
                                )
                                eventItem2.visibility = View.VISIBLE
                                eventItem2.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[1].start_date.substring(10, 16),
                                    eventList[1].client_name.toUpperCase()
                                )
                                eventItem3.visibility = View.VISIBLE
                                eventItem3.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[2].start_date.substring(10, 16),
                                    eventList[2].client_name.toUpperCase()
                                )
                                moreEvents.visibility = View.VISIBLE
                            }
                        }
                    }



                    textView.text = day.date.dayOfMonth.toString()

                    if (day.owner == DayOwner.THIS_MONTH) {
                        when (day.date) {
                            today -> {
                                layout.setBackgroundResource(R.drawable.today_date_background)
                                sotIndicator.visibility = View.VISIBLE
                            }

                            else -> {
                                layout.background = null
                            }
                        }


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
                                ditetEJaves[index]
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


