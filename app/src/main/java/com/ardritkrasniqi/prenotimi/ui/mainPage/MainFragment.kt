package com.ardritkrasniqi.prenotimi.ui.mainPage


import android.annotation.SuppressLint
import android.os.Build
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.R.color.event_gone_color
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.ardritkrasniqi.prenotimi.utils.daysOfWeekFromLocale
import com.ardritkrasniqi.prenotimi.utils.setTextColorRes
import com.ardritkrasniqi.prenotimi.utils.stringToLocalDate
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.calendar_days_header.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.io.Serializable
import java.util.*

class MainFragment : Fragment() {

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private lateinit var allAppointments: MutableList<Event>
    private lateinit var nextMonthButton: ImageView
    private lateinit var previousMonthButton: ImageView
    private lateinit var currentMothText: TextView
    private lateinit var viewModel: MainViewModel
    private lateinit var calendarView: CalendarView
    private var currentMonthForCheck: Int? = null
    private lateinit var bundle: Bundle
    private var isAppointmentAdded = false
    private val today = LocalDate.now()
    val ditetEJaves = arrayOf("Dielë", "Hënë", "Martë", "Mërkurë", "Enjte", "Premte", "Shtunë")

    private var getAppointmentsExectued = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        bundle = Bundle()
        isAppointmentAdded = bundle.getBoolean("isAppointmentAdded")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        //shared preference stuff
        val sharedPreferences = PreferenceProvider(this.requireContext())

        if (sharedPreferences.getToken().isNullOrEmpty()) {
            findNavController().navigate(R.id.action_mainFragment_to_authFragment)

        }


        viewModel.token.value = "Bearer ${sharedPreferences.getToken()}"


        if (!getAppointmentsExectued) {
            viewModel.getAppointments()
            getAppointmentsExectued = true
        }
        calendarView = view.findViewById(R.id.calendarView)
        allAppointments = mutableListOf()

        viewModel.allAppointments.observe(viewLifecycleOwner, Observer { list ->
            allAppointments = list as MutableList<Event>
            if(!isAppointmentAdded){
                val lastAppointment = allAppointments.last()
                Snackbar.make(view, "Rezervimi per ${lastAppointment.client_name}, prej ${lastAppointment.startTime} deri ${lastAppointment.endTime} u krijua", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.infoColor, null))
                    .setAction(R.string.shko_te_rezervimi, View.OnClickListener {
                }).show()
            }
            calendarView.notifyCalendarChanged()
        })





        nextMonthButton = activity?.findViewById(R.id.nextMonthButton)!!
        nextMonthButton.visibility = View.VISIBLE
        previousMonthButton = activity?.findViewById(R.id.previousMonthButton)!!
        previousMonthButton.visibility = View.VISIBLE
        currentMothText = activity?.findViewById(R.id.monthYear_text)!!
        currentMothText.visibility = View.VISIBLE


        // formatuesi i tekstit te muajit ne toolbar
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")


        val daysOfWeek: Array<DayOfWeek> =
            daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        calendarView.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            daysOfWeek.first()
        )

        calendarView.scrollToMonth(currentMonth)

        calendarView.maxRowCount = 6

        class DayViewContainer(view: View) : ViewContainer(view), Serializable {
            lateinit var day: CalendarDay
            var textView: TextView = view.calendarDay_text
            var layout: ConstraintLayout = view.calendarDay_layout
            val sotIndicator: ImageView = view.sot_indicator
            val eventItem1 = view.event_item_1
            val eventItem2 = view.event_item_2
            val eventItem3 = view.event_item_3
            val moreEvents = view.more_appointments_dots





            // klikimi mbi qdo qeli te kalendarit
            init {

                view.setOnClickListener {
                    val dayList = allAppointments.filter {
                        stringToLocalDate(it.start_date.substring(0, 10)) == (day.date)
                    }
                    bundle.putParcelableArrayList(
                        "rezervimet",
                        dayList as ArrayList<out Parcelable>
                    )
                    bundle.putString("DATE", day.date.toString())
                    view.findNavController()
                        .navigate(R.id.action_mainFragment2_to_dayFragment, bundle)
                }
            }
        }

        Log.i("added appointment", isAppointmentAdded.toString())





        // krijimi e diteve te kalendarit
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            @SuppressLint("ResourceAsColor")
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                var eventItemCounter = 0
                val eventList = mutableListOf<Event>()
                val res = resources

                allAppointments.sortBy { it.start_date }


                for (i in allAppointments) {
                    if (stringToLocalDate(i.start_date.substring(0, 10)) == (day.date)) {
                        eventItemCounter++
                        eventList.add(i)
                    }
                }



                val textView = container.textView
                val layout = container.layout
                val sotIndicator = container.sotIndicator
                val eventItem1 = container.eventItem1
                val eventItem2 = container.eventItem2
                val eventItem3 = container.eventItem3
                val moreEvents = container.moreEvents

                if (day.owner == DayOwner.THIS_MONTH && day.date.monthValue == currentMonthForCheck) {


                    if (eventItemCounter > 0) {
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
                                    eventList[1].client_name.toUpperCase(Locale.getDefault())
                                )
                                eventItem3.visibility = View.VISIBLE
                                eventItem3.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[2].start_date.substring(10, 16),
                                    eventList[2].client_name.toUpperCase(Locale.getDefault())
                                )
                            }
                            else -> {
                                eventItem1.visibility = View.VISIBLE
                                eventItem1.text = String.format(
                                    res.getString(R.string.rezervimet_preview),
                                    eventList[0].start_date.substring(10, 16),
                                    eventList[0].client_name.toUpperCase(Locale.getDefault())
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
                // if its earlier than today appointpreviews become grey :D
                if (day.date < today) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        eventItem1.setBackgroundColor(
                            resources.getColor(
                                event_gone_color,
                                null
                            )
                        )
                        eventItem2.setBackgroundColor(
                            resources.getColor(
                                event_gone_color,
                                null
                            )
                        )
                        eventItem3.setBackgroundColor(
                            resources.getColor(
                                event_gone_color,
                                null
                            )
                        )
                    } else {
                        eventItem1.setBackgroundColor(event_gone_color)
                        eventItem2.setBackgroundColor(event_gone_color)
                        eventItem3.setBackgroundColor(event_gone_color)
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
            currentMothText.text = title
            currentMonthForCheck = month.yearMonth.month.value
            calendarView.notifyCalendarChanged()


            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                calendarView.notifyDateChanged(it)
                calendarView.notifyCalendarChanged()
            }
        }



        nextMonthButton.setOnClickListener {
            calendarView.findFirstVisibleMonth()?.let {
                calendarView.smoothScrollToMonth(it.yearMonth.next)
            }

        }
        previousMonthButton.setOnClickListener {
            calendarView.findFirstVisibleMonth()?.let {
                calendarView.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }




}


