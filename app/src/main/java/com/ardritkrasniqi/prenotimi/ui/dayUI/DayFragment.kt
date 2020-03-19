package com.ardritkrasniqi.prenotimi.ui.dayUI


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentDayBinding
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.ui.costomViews.CalendarDayView

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



        binding.dayView.setLimitTime(7, 22)

        events = ArrayList()
        events.add(
            Event(
                "Ardrit Krasniqi", "0343434",
                "2020-03-10 16:40:01", "2020-03-10 18:20:00", 0, "fuck off"
            )
        )

        events.add(
            Event(
                "Mergim Krasniqi", "0343434",
                "2020-03-10 09:40:01", "2020-03-10 12:20:00", 0, "hehehhe"
            )
        )

        binding.dayView.setEvents(events)

        return binding.root

    }
}