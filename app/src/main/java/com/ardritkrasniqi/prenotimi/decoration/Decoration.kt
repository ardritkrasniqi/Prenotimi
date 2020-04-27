package com.ardritkrasniqi.prenotimi.decoration

import android.content.Context
import android.graphics.Rect
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import com.ardritkrasniqi.prenotimi.ui.costomViews.DayView
import com.ardritkrasniqi.prenotimi.ui.costomViews.EventView

class Decoration(private var context: Context) : IDecoration {
     var eventClickListener: EventView.OnEventClickListener? = null

    override fun getEventView(
        event: IEvent?, eventBound: Rect?, hourHeight: Int,
        seperateHeight: Int
    ): EventView {
        val eventView = EventView(context)
        if (event != null) {
            eventView.setEvent(event)
        }
        if (eventBound != null) {
            eventView.setPosition(eventBound, -hourHeight, hourHeight - seperateHeight * 2)
        }
        eventClickListener?.let { eventView.setOnEventClickListener(it) }
        return eventView
    }


    fun setOnEventClickListener(listener: EventView.OnEventClickListener) {
        this.eventClickListener = listener
    }

    override fun getDayView(hour: Int): DayView {
        val dayView = DayView(context)
        dayView.setText(String.format("%1$2s:00", hour))
        return dayView
    }





}
