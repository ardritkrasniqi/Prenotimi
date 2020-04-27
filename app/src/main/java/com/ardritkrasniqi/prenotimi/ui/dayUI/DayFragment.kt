package com.ardritkrasniqi.prenotimi.ui.dayUI


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ardritkrasniqi.prenotimi.databinding.FragmentDayBinding
import com.ardritkrasniqi.prenotimi.decoration.Decoration
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import com.ardritkrasniqi.prenotimi.ui.costomViews.CalendarDayView
import com.ardritkrasniqi.prenotimi.ui.costomViews.EventView
import com.ardritkrasniqi.prenotimi.ui.costomViews.EventView.OnEventClickListener

class DayFragment : Fragment() {
    private lateinit var events: MutableList<Event>
    private lateinit var dayView: CalendarDayView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDayBinding.inflate(inflater)
        dayView = binding.dayView


        dayView.setLimitTime(6, 23)

        events = mutableListOf()

        val bundle = arguments
        val eventList = bundle?.get("rezervimet") as List<Event>
        for (element in eventList) {
            events.add(element)
        }





        (dayView.getDecoration() as Decoration?)?.setOnEventClickListener(
            object : OnEventClickListener {
                override fun onEventClick(view: EventView, data: IEvent) {
                    Log.i("TAG", data.clientName)
                }

                override fun onEventViewClick(view: View, eventView: EventView, data: IEvent) {
                    Log.i("TAG", "${data.clientName}, jajaj")
                    view.findNavController().navigate(
                        DayFragmentDirections.actionDayFragmentToShtoRezervimDialog(
                            data as Event, true
                        )
                    )
                }
            })


        dayView.setEvents(events)

        return binding.root
    }


}

