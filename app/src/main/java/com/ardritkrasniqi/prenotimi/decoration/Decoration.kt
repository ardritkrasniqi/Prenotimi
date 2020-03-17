package com.ardritkrasniqi.prenotimi.decoration

import android.content.Context
import android.graphics.Rect
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import com.ardritkrasniqi.prenotimi.ui.costomViews.DayView
import com.ardritkrasniqi.prenotimi.ui.costomViews.EventView

class CdvDecorationDefault(protected var mContext: Context) : CdvDecoration {
     var mEventClickListener: EventView.OnEventClickListener? = null

    override fun getEventView(
        event: IEvent?, eventBound: Rect?, hourHeight: Int,
        seperateHeight: Int
    ): EventView {
        val eventView = EventView(mContext)
        if (event != null) {
            eventView.setEvent(event)
        }
        if (eventBound != null) {
            eventView.setPosition(eventBound, -hourHeight, hourHeight - seperateHeight * 2)
        }
        eventView.setOnEventClickListener(mEventClickListener)
        return eventView
    }



    override fun getDayView(hour: Int): DayView {
        val dayView = DayView(mContext)
        dayView.setText(String.format("%1$2s:00", hour))
        return dayView
    }

    fun setOnEventClickListener(listener: EventView.OnEventClickListener?) {
        mEventClickListener = listener
    }



}
