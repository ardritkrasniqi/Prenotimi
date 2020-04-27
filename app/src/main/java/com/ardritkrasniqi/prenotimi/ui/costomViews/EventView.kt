package com.ardritkrasniqi.prenotimi.ui.costomViews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import java.util.*


class EventView : FrameLayout {
    private lateinit var eventi: IEvent
    private lateinit var eventClickListener: OnEventClickListener
    private lateinit var eventContent: ConstraintLayout
    private lateinit var eventName: TextView
    private lateinit var topTime: TextView
    private lateinit var bottomTime:TextView


    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_event, this, true)
        eventContent = findViewById(R.id.item_event_content)
        eventName = findViewById(R.id.item_event_name)
        topTime = findViewById(R.id.top_time)
        bottomTime = findViewById(R.id.bottom_time)
        super.setOnClickListener {
            eventClickListener.onEventClick(this@EventView, eventi)
        }
        val eventItemClickListener =
            OnClickListener { v ->
                eventClickListener.onEventViewClick(v, this@EventView, eventi)
            }

        eventContent.setOnClickListener(eventItemClickListener)
    }

    fun setOnEventClickListener(listener: OnEventClickListener) {
        eventClickListener = listener
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw UnsupportedOperationException("you should use setOnEventClickListener()")
    }

    fun setEvent(event: IEvent) {
        eventi = event
        val res = resources
        eventName.text = String.format(
            res.getString(R.string.details_placeholder),
            event.clientName.toUpperCase(Locale.getDefault()),
            event.commenti
        )
        topTime.text = event.startTime.substring(0,6)
        bottomTime.text = event.endTime.subSequence(0,6)

    }



    fun setPosition(rect: Rect, topMargin: Int, bottomMargin: Int) {
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = (rect.top - 0 - 8 + topMargin)
        params.height = (rect.height()
                + 0
                + 8
                + bottomMargin
                + resources.getDimensionPixelSize(R.dimen.cdv_extra_dimen))
        params.leftMargin = rect.left
        params.rightMargin = 3
        layoutParams = params
    }

    interface OnEventClickListener {
        fun onEventClick(view: EventView, data: IEvent)
        fun onEventViewClick(
            view: View,
            eventView: EventView,
            data: IEvent
        )
    }
}