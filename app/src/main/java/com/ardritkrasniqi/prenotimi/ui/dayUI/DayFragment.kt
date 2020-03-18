package com.ardritkrasniqi.prenotimi.ui.dayUI


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentDayBinding
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.ui.costomViews.CalendarDayView
import com.ardritkrasniqi.prenotimi.utils.timeConverterStringToCalendar

private lateinit var events: ArrayList<Event>

class DayFragment : Fragment() {
    var dayView: CalendarDayView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDayBinding>(
            inflater,
            R.layout.fragment_day,
            container,
            false
        )



        binding.dayView.setLimitTime(9, 22)

        events = ArrayList()
        events.add(Event(
            "Ardrit Krasniqi", "0343434",
            "11:00", "12:00", 0, "fuck off"
        ))

        binding.dayView.setEvents(events)

        return binding.root

    }
}