package com.ardritkrasniqi.prenotimi.ui.costomViews

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import java.util.*


/*
Ardrit Krasniqi 2020.
 */


class EventView : FrameLayout {
    private lateinit var eventi: IEvent
    private lateinit var eventClickListener: OnEventClickListener
    private lateinit var eventContent: ConstraintLayout
    private lateinit var eventName: TextView
    private lateinit var topTime: TextView
    private lateinit var bottomTime: TextView

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
        throw UnsupportedOperationException("perdore setOnEventClickListener()")
    }

    //populon nje event me te dhenat perkatese
    fun setEvent(event: Event) {
        eventi = event
        eventName.text = String.format(
            resources.getString(R.string.details_placeholder),
            event.clientName.toUpperCase(Locale.getDefault()),
            event.commenti, event.nrTel
        )
        topTime.text = event.startTime.substring(0, 6)
        bottomTime.text = event.endTime.subSequence(0, 6)
        eventContent.setBackgroundColor(Color.parseColor(event.getEventColor()))
    }

    // merr rektin, top dhe bottom margin (nese ka) dhe e vendos ne view
    fun setPosition(rect: Rect, topMargin: Int, bottomMargin: Int) {
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = (rect.top)
        params.height = (rect.height())
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