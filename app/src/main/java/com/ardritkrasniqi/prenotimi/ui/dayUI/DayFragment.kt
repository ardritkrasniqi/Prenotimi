package com.ardritkrasniqi.prenotimi.ui.dayUI


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ardritkrasniqi.prenotimi.databinding.FragmentDayBinding
import com.ardritkrasniqi.prenotimi.model.Event

import com.ardritkrasniqi.prenotimi.ui.costomViews.CalendarDayView

class DayFragment : Fragment() {

    private lateinit var events: MutableList<Event>
    var dayView: CalendarDayView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDayBinding.inflate(inflater)


        binding.dayView.setLimitTime(7, 22)

        events = mutableListOf()

        val bundle = arguments
        bundle?.getParcelableArrayList<Event>("rezervminet")

        Log.i("REZERVIMET", bundle?.get("rezervimet").toString())


        val eventList = bundle?.get("rezervimet") as List<Event>
        for(element in eventList){
            events.add(element)
        }




        binding.dayView.setEvents(events)

        return binding.root

    }
}

