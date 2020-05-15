package com.ardritkrasniqi.prenotimi.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.prenotimi.databinding.CalendarDayBinding
import com.kizitonwose.calendarview.model.CalendarDay

class MainFragmentAdapter(
    private val calendarDay: CalendarDay
) : RecyclerView.Adapter<MainFragmentAdapter.DayViewContainer>() {

    private lateinit var day: CalendarDay

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewContainer {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DayViewContainer, position: Int) {
        TODO("Not yet implemented")
    }

    inner class DayViewContainer(
        private val recyclerViewDayBinding: CalendarDayBinding
    ) : RecyclerView.ViewHolder(recyclerViewDayBinding.root) {

    }


}

