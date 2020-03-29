package com.ardritkrasniqi.prenotimi.ui.costomViews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.model.data.IEvent
import java.lang.String


class EventView : FrameLayout {
    var mEvent: IEvent? = null
    var mEventClickListener: OnEventClickListener? = null
    var mEventHeader: RelativeLayout? = null
    var mEventContent: LinearLayout? = null
    var mEventHeaderText1: TextView? = null
    var mEventHeaderText2: TextView? = null
    var mEventName: TextView? = null

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
        mEventHeader = findViewById<View>(R.id.item_event_header) as RelativeLayout
        mEventContent = findViewById<View>(R.id.item_event_content) as LinearLayout
        mEventName = findViewById<View>(R.id.item_event_name) as TextView
        mEventHeaderText1 =
            findViewById<View>(R.id.item_event_header_text1) as TextView
        mEventHeaderText2 =
            findViewById<View>(R.id.item_event_header_text2) as TextView
        super.setOnClickListener {
            if (mEventClickListener != null) {
                mEventClickListener!!.onEventClick(this@EventView, mEvent)
            }
        }
        val eventItemClickListener =
            OnClickListener { v ->
                if (mEventClickListener != null) {
                    mEventClickListener!!.onEventViewClick(v, this@EventView, mEvent)
                }
            }
        mEventHeaderText1!!.setOnClickListener(eventItemClickListener)
        mEventHeaderText2!!.setOnClickListener(eventItemClickListener)
        mEventContent!!.setOnClickListener(eventItemClickListener)
    }

    fun setOnEventClickListener(listener: OnEventClickListener?) {
        mEventClickListener = listener
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw UnsupportedOperationException("you should use setOnEventClickListener()")
    }

    fun setEvent(event: IEvent) {
        mEvent = event
        mEventName?.text = String.valueOf(event.clientName)
        mEventContent?.setBackgroundColor(R.color.eventColor)
    }

    private val headerHeight: Int
        get() = mEventHeader!!.measuredHeight

    private val headerPadding: Int
        get() = mEventHeader!!.paddingBottom + mEventHeader!!.paddingTop

    fun setPosition(rect: Rect, topMargin: Int, bottomMargin: Int) {
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = (rect.top - headerHeight - headerPadding + topMargin
                - resources.getDimensionPixelSize(R.dimen.cdv_extra_dimen))
        params.height = (rect.height()
                + headerHeight
                + headerPadding
                + bottomMargin
                + resources.getDimensionPixelSize(R.dimen.cdv_extra_dimen))
        params.leftMargin = rect.left
        layoutParams = params
    }

    interface OnEventClickListener {
        fun onEventClick(view: EventView?, data: IEvent?)
        fun onEventViewClick(
            view: View?,
            eventView: EventView?,
            data: IEvent?
        )
    }
}