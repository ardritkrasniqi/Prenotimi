package com.ardritkrasniqi.prenotimi.decoration

import android.graphics.Rect
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import com.ardritkrasniqi.prenotimi.ui.costomViews.DayView
import com.ardritkrasniqi.prenotimi.ui.costomViews.EventView

interface IDecoration {
    fun getEventView(
        event: IEvent?,
        eventBound: Rect?,
        hourHeight: Int,
        seperateHeight: Int
    ): EventView?



    fun getDayView(hour: Int): DayView?

}