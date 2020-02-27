package com.ardritkrasniqi.prenotimi.ui.costomViews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.dpToPx
import com.ardritkrasniqi.prenotimi.utils.screenWidth

private lateinit var dayColumn: RelativeLayout
private lateinit var eventRelativeLayout: RelativeLayout
private lateinit var eventLinearLayout: LinearLayout
private lateinit var horizontalScrollView: HorizontalScrollView

class EventDay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var hourTextWidth: Float = 0f
    private var hourTextFirstTopPadding: Float = 0f
    private var hourTextPadding: Float = 0f

    private var eventLineFirstTopPadding: Float = 0f
    private var eventLinePadding: Float = 0f

    private var hourColor: Int = 0


    init {

        this.orientation = HORIZONTAL
        val styledAttributes = context.theme.obtainStyledAttributes(0, R.styleable.EventDay)

        try {
            hourTextWidth =
                styledAttributes.getFloat(R.styleable.EventDay_hourTextWidth, 80.toFloat())
            hourTextFirstTopPadding = styledAttributes.getFloat(
                R.styleable.EventDay_hourTextFirstTopPadding,
                45.toFloat()
            )
            hourTextPadding =
                styledAttributes.getFloat(R.styleable.EventDay_hourTextPadding, 60.toFloat())
            hourColor = styledAttributes.getColor(R.styleable.EventDay_hourColor, Color.BLACK)

            eventLineFirstTopPadding = styledAttributes.getFloat(
                R.styleable.EventDay_eventLineFirstTopPadding,
                60.toFloat()
            )
            eventLinePadding =
                styledAttributes.getFloat(R.styleable.EventDay_eventLinePadding, 60.toFloat())


        } finally {
            styledAttributes.recycle()
        }

        val hours = context.resources.getTextArray(R.array.hours)
        dayColumn = RelativeLayout(context)
        eventRelativeLayout = RelativeLayout(context)
        eventLinearLayout = LinearLayout(context)
        horizontalScrollView = HorizontalScrollView(context)
        horizontalScrollView.isVerticalScrollBarEnabled = false
        horizontalScrollView.isHorizontalScrollBarEnabled = true

        val dayColumnParams = RelativeLayout.LayoutParams(
            dpToPx(hourTextWidth.toInt(),context),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val eventColumnParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        eventRelativeLayout.layoutParams = eventColumnParams

        dayColumn.layoutParams = dayColumnParams


        createHourText(
            context,
            (dpToPx(hourTextFirstTopPadding.toInt(), context)),
            hours[0].toString()
        )
        createEventLine(context, (dpToPx(eventLineFirstTopPadding.toInt(), context)), false)


        for(i in 0..24){
            if(i == 24){
                createHourText(context, dpToPx(hourTextPadding.toInt(),context), "")
                createEventLine(context, dpToPx(eventLinePadding.toInt(),context), true)
            } else {
                createHourText(context, dpToPx(hourTextPadding.toInt(),context), hours[i].toString())
                createEventLine(context, dpToPx(eventLinePadding.toInt(),context), false)

            }
        }

        eventLinearLayout.addView(eventRelativeLayout)
        this.addView(dayColumn)




    }



    private fun createHourText(context: Context, paddingTop: Int, text: String) {
        val hourText = TextView(context)
        val hourTextParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        hourTextParams.gravity = Gravity.CENTER_HORIZONTAL
        hourText.layoutParams = hourTextParams
        hourText.gravity = Gravity.CENTER_HORIZONTAL
        hourText.setPadding(0, paddingTop, 0, 0)
        hourText.setTextColor(hourColor)
        hourText.text = text
    }


    private fun createEventLine(context: Context, marginTop: Int, lastLine: Boolean) {
        val eventLine = ImageView(context)
        val eventLineParams: RelativeLayout.LayoutParams =
            RelativeLayout.LayoutParams(screenWidth(context), dpToPx(1, context))
        eventLineParams.setMargins(0, marginTop, (dpToPx(20, context)), 0)
        if (!lastLine) {
            eventLine.setBackgroundColor(R.styleable.EventDay_hourColor)
        } else {
            eventLine.setBackgroundColor(Color.BLUE)
        }
        eventLine.layoutParams = eventLineParams
        eventRelativeLayout.addView(eventLine)
    }





}


