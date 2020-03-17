package com.ardritkrasniqi.prenotimi.ui.costomViews

import android.content.Context
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.ardritkrasniqi.prenotimi.R

class DayView : FrameLayout {
    private var mTextHour: TextView? = null
    private var mSeparateHour: LinearLayout? = null

    constructor(context: Context?) : super((context)!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        (context)!!,
        attrs
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super((context)!!, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_day, this, true)
        mTextHour = findViewById<View>(R.id.text_hour) as TextView
        mSeparateHour = findViewById<View>(R.id.separate_hour) as LinearLayout
    }

    fun setText(text: String?) {
        mTextHour!!.text = text
    }

    val hourTextWidth: Float
        get() {
            val param: LinearLayout.LayoutParams =
                mTextHour!!.layoutParams as LinearLayout.LayoutParams
            val measureTextWidth: Float = mTextHour!!.paint.measureText("12:00")
            return (measureTextWidth.coerceAtLeast(param.width.toFloat())
                    + param.marginEnd
                    + param.marginStart)
        }

    val hourTextHeight: Float
        get() = (StaticLayout(
            "12:00", mTextHour!!.paint, hourTextWidth.toInt(),
            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)).height.toFloat()

    val separateHeight: Float
        get() {
            return mSeparateHour!!.layoutParams.height.toFloat()
        }

    private fun setHourSeparatorAsInvisible() {
        mSeparateHour!!.visibility = View.INVISIBLE
    }

    private fun setHourSeparatorAsVisible() {
        mSeparateHour!!.visibility = View.VISIBLE
    }

    fun setHourSeparatorIsVisible(b: Boolean) {
        if (b) {
            setHourSeparatorAsVisible()
        } else {
            setHourSeparatorAsInvisible()
        }
    }
}