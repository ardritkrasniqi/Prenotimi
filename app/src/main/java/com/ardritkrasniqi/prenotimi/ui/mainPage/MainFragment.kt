package com.ardritkrasniqi.prenotimi.ui.mainPage


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.ardritkrasniqi.prenotimi.utils.screenDimensions
import com.ardritkrasniqi.prenotimi.utils.setAppointmentsOnScreen
import com.ardritkrasniqi.prenotimi.utils.setTextColorRes
import com.ardritkrasniqi.prenotimi.utils.stringToLocalDate
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.calendar_days_header.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.io.Serializable
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var selectedDate: LocalDate? = null
    lateinit var allAppointments: MutableList<Event>
    private lateinit var nextMonthButton: ImageView
    private lateinit var previousMonthButton: ImageView
    private lateinit var currentMothText: TextView
    private lateinit var viewModel: MainViewModel
    private lateinit var goToday: ImageView
    private lateinit var calendarView: CalendarView
    private var currentMonthForCheck: Int? = null
    private lateinit var bundle: Bundle
    private lateinit var monthListener: YearMonth
    private val today = LocalDate.now()
    private var monthYears: String? = null
    private lateinit var monthTitleFormatter: DateTimeFormatter
    private lateinit var eventItemList: MutableList<TextView>
    val currentMonth = YearMonth.now()

    val ditetEJaves =
        arrayOf("E Hënë", "E Martë", "E Mërkurë", "E Enjte", "E Premte", "E Shtunë", "E Dielë")

    private var getAppointmentsExectued = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        bundle = Bundle()

        monthYears = this.arguments?.getString("MONTH")
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
        goToday = view.findViewById(R.id.goToToday)
        goToday.setOnClickListener {
            calendarView.smoothScrollToDate(today)
        }
        viewModel.getAppointments()

        calendarView = view.findViewById(R.id.calendarView)
        allAppointments = mutableListOf()

        nextMonthButton = activity?.findViewById(R.id.nextMonthButton)!!
        nextMonthButton.visibility = View.VISIBLE
        previousMonthButton = activity?.findViewById(R.id.previousMonthButton)!!
        previousMonthButton.visibility = View.VISIBLE
        currentMothText = activity?.findViewById(R.id.monthYear_text)!!
        currentMothText.visibility = View.VISIBLE
        // formatuesi i tekstit te muajit ne toolbar
        monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

        if (sharedPreferences.getIsWeekCalendar()) {
            monthToWeek()
        } else {
            calendarView.setup(
                viewModel.firstMonth,
                viewModel.lastMonth,
                viewModel.firstDayOfWeek
            )
            calendarView.maxRowCount = 6
            if (monthYears != null) {
                calendarView.scrollToMonth(YearMonth.parse(monthYears))
            } else {
                calendarView.scrollToMonth(currentMonth)
            }
        }


        viewModel.allAppointments.observe(viewLifecycleOwner, Observer { list ->

            allAppointments = list as MutableList<Event>
            calendarView.notifyCalendarChanged()

            if (sharedPreferences.getIsappointmentAdded()) {
                appointmentIsAddedSnack(allAppointments, calendarView)
                sharedPreferences.saveIsAppointmentAdded(false)
            }
        })

        viewModel.incrementingMonthNumber.observe(viewLifecycleOwner, Observer {
            if (it == 5 && calendarView.maxRowCount != 1) {
                val monthBundle = Bundle()
                monthBundle.putString("MONTH", monthListener.toString())
                findNavController().navigate(R.id.action_mainFragment_self, monthBundle)
            }
        })




        class DayViewContainer(view: View) : ViewContainer(view), Serializable {
            lateinit var day: CalendarDay
            var textView: TextView = view.calendarDay_text
            var layout: ConstraintLayout = view.calendarDay_layout
            val sotIndicator: ImageView = view.sot_indicator
            val eventItem1 = view.event_item_1
            val eventItem2 = view.event_item_2
            val eventItem3 = view.event_item_3
            val eventItem4 = view.event_item_4
            val eventItem5 = view.event_item_5
            val eventItem6 = view.event_item_6
            val eventItem7 = view.event_item_7
            val eventItem8 = view.event_item_8
            val eventItem9 = view.event_item_9
            val eventItem10 = view.event_item_10
            val eventItem11 = view.event_item_11
            val eventItem12 = view.event_item_12
            val eventItem13 = view.event_item_13
            val eventItem14 = view.event_item_14
            val eventItem15 = view.event_item_15


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

                eventList.sortBy { it.start_date }

                val textView = container.textView
                val layout = container.layout
                val sotIndicator = container.sotIndicator
                val eventItem1 = container.eventItem1
                val eventItem2 = container.eventItem2
                val eventItem3 = container.eventItem3
                val eventItem4 = container.eventItem4
                val eventItem5 = container.eventItem5
                val eventItem6 = container.eventItem6
                val eventItem7 = container.eventItem7
                val eventItem8 = container.eventItem8
                val eventItem9 = container.eventItem9
                val eventItem10 = container.eventItem10
                val eventItem11 = container.eventItem11
                val eventItem12 = container.eventItem12
                val eventItem13 = container.eventItem13
                val eventItem14 = container.eventItem14
                val eventItem15 = container.eventItem15
                val moreEvents = container.moreEvents


                eventItemList = mutableListOf()
                eventItemList.apply {
                    add(eventItem1)
                    add(eventItem2)
                    add(eventItem3)
                    add(eventItem4)
                    add(eventItem5)
                    add(eventItem6)
                    add(eventItem7)
                    add(eventItem8)
                    add(eventItem9)
                    add(eventItem10)
                    add(eventItem11)
                    add(eventItem12)
                    add(eventItem13)
                    add(eventItem14)
                    add(eventItem15)
                }

                if (day.owner == DayOwner.THIS_MONTH) {
                    if (eventItemCounter > 0) {
                        when (sharedPreferences.getIsWeekCalendar()) {
                            true -> {
                                setAppointmentsOnScreen(
                                    false,
                                    res,
                                    eventItemCounter,
                                    eventItemList,
                                    eventList,
                                    requireContext()
                                )
                            }

                            // when isNotWeekCalendar(MonthCalendar)
                            false -> {
                                setAppointmentsOnScreen(
                                    true,
                                    res,
                                    eventItemCounter,
                                    eventItemList,
                                    eventList,
                                    requireContext()
                                )
                                if (eventItemCounter > 3) {
                                    moreEvents.visibility = View.VISIBLE
                                }
                            }
                        }
                    }


                }
                // if its earlier than today appointpreviews become grey :D
                if (day.date < today) {

                    eventItem1.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )
                    eventItem2.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )
                    eventItem3.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )
                    eventItem4.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )
                    eventItem5.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )
                    eventItem6.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )
                    eventItem7.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )
                    eventItem8.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.text_grey)
                    )


                }
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    when (day.date) {
                        today -> {
                            sotIndicator.visibility = View.VISIBLE
                        }
                        else -> {
                            layout.background = null
                        }
                    }


                } else {
                    // per datat te cilat nuk i perkasin muajit i cili eshte i paraqitur
                    textView.setTextColorRes(R.color.almostWhite)
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
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        }
                    month.yearMonth
                }
            }
        }

        fun titleForMonth(calendarMonth: CalendarMonth): String {
            val firstDate = calendarMonth.weekDays.first().first().date
            val lastDate = calendarMonth.weekDays.last().last().date
            return if (sharedPreferences.getIsWeekCalendar()) {
                if (firstDate.month != lastDate.month) {
                    "${monthTitleFormatter.format(firstDate)
                        .capitalize()} / ${monthTitleFormatter.format(lastDate).capitalize()}"
                } else {
                    monthTitleFormatter.format(firstDate).capitalize()
                }
            } else {
                "${monthTitleFormatter.format(calendarMonth.yearMonth)
                    .capitalize()} ${calendarMonth.yearMonth.year}"
            }
        }

        calendarView.monthScrollListener = { month ->
            monthListener = month.yearMonth
            currentMothText.text = titleForMonth(month)
            currentMonthForCheck = month.yearMonth.monthValue
            viewModel.incrementMonthNumber()
            calendarView.notifyCalendarChanged()

            if (month.yearMonth.monthValue == today.monthValue) {
                goToday.visibility = View.GONE
            } else {
                goToday.visibility = View.VISIBLE
            }
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

    private fun appointmentIsAddedSnack(allAppointments: List<Event>, calendarView: CalendarView) {
        val lastItemAdded = allAppointments.last()
        view?.let {
            val snackbar = Snackbar.make(it, "", Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.infoColor))
                .setText(
                    "Rezervimi për ${lastItemAdded.client_name},prej ${lastItemAdded.startTime.substring(
                        0,
                        6
                    )} deri ${lastItemAdded.endTime.substring(0, 6)} u shtua"
                )
                .setAction("Shko tek rezervimi", View.OnClickListener {
                    calendarView.smoothScrollToDate(
                        stringToLocalDate(
                            allAppointments.last().start_date.substring(
                                0,
                                10
                            )
                        )
                    )
                })
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.black_color))
                .show()
        }
    }

    fun navigating() {
        bundle.putParcelableArrayList(
            "appointments",
            allAppointments as ArrayList<out Parcelable>
        )
        findNavController().navigate(R.id.action_mainFragment_to_listAppointments, bundle)
    }

    private fun monthToWeek() {
        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        calendarView.dayWidth = dm.widthPixels / 7
        calendarView.apply {
            maxRowCount = 1
            inDateStyle = InDateStyle.ALL_MONTHS
            outDateStyle = OutDateStyle.END_OF_ROW
            dayHeight = screenDimensions("height", requireContext())
            scrollMode = ScrollMode.PAGED
            hasBoundaries = false
            // Value for firstDayOfWeek does not matter since inDates and outDates are not generated
            calendarView.setup(currentMonth, currentMonth.plusMonths(2), viewModel.firstDayOfWeek)
            scrollToDate(LocalDate.now())
            notifyCalendarChanged()
        }
        previousMonthButton.visibility = View.GONE
        nextMonthButton.visibility = View.GONE
    }



    fun refreshFragment() {
        findNavController().navigate(R.id.action_mainFragment_self)
    }


}



