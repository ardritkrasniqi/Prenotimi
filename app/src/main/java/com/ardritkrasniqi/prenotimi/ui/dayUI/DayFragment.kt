package com.ardritkrasniqi.prenotimi.ui.dayUI


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentDayBinding
import com.ardritkrasniqi.prenotimi.decoration.Decoration
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import com.ardritkrasniqi.prenotimi.ui.costomViews.CalendarDayView
import com.ardritkrasniqi.prenotimi.ui.costomViews.EventView
import com.ardritkrasniqi.prenotimi.ui.costomViews.EventView.OnEventClickListener
import com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog.ShtoRezervimDialogDirections
import com.ardritkrasniqi.prenotimi.utils.DrawerLocker
import kotlinx.android.synthetic.main.calendar_day.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.days
import kotlin.time.hours


class DayFragment : Fragment() {
    private lateinit var events: MutableList<Event>
    private lateinit var dayView: CalendarDayView
    private lateinit var nextMonthButton: ImageView
    private lateinit var previousMonthButton: ImageView
    private lateinit var currentMothText: TextView

    var date: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDayBinding.inflate(inflater)
        dayView = binding.dayView

        dayView.setLimitTime(0, 23)

        events = mutableListOf()

        val bundle = arguments
        val eventList = bundle?.get("rezervimet") as List<Event>
        for (element in eventList) {
            events.add(element)
        }
        date = bundle.get("DATE") as String

        (dayView.getDecoration() as Decoration?)?.setOnEventClickListener(
            object : OnEventClickListener {
                override fun onEventClick(view: EventView, data: IEvent) {
                }

                override fun onEventViewClick(view: View, eventView: EventView, data: IEvent) {
                    view.findNavController().navigate(
                        DayFragmentDirections.actionDayFragmentToShtoRezervimDialog(
                            data as Event, true,
                        null)
                    )
                }
            })



        dayView.setEvents(events)

        (activity as DrawerLocker?)?.setDrawerLocked(true)


        nextMonthButton = activity?.findViewById(R.id.nextMonthButton)!!
        nextMonthButton.visibility = View.GONE
        previousMonthButton = activity?.findViewById(R.id.previousMonthButton)!!
        previousMonthButton.visibility = View.GONE
        currentMothText = activity?.findViewById(R.id.monthYear_text)!!
        currentMothText.text = dateFormaterForToolbar(date)


        return binding.root
    }


    private fun dateFormaterForToolbar(date: String): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatter.parse(date)
        return SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault()).format(date)

    }






}

