package com.ardritkrasniqi.timelinetest

import androidx.core.content.ContextCompat
import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import kotlin.math.roundToInt
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.ardritkrasniqi.prenotimi.R
import java.sql.Time


private const val NUMBER_OF_ROWS = 24

class CustomView : ViewGroup {
    var eachHourHeightInDp = 118f
    var minimumHeightEachSellPercentage = .25f
    private var maxChildrenEnd = 0
    private var dividerStartOffset = 108f
    private lateinit var dividerPaint: Paint
    private lateinit var textPaint: Paint
    private var dividerColorId: Int = R.color.black_color
    private var dividerTextColorId: Int = R.color.black_color
    private lateinit var dividerTitles: MutableList<String>


    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }


    @SuppressLint("CustomViewStyleable")
    fun init(set: AttributeSet?) {

        setWillNotDraw(false)
        layoutTransition = LayoutTransition()
        //if user not set any divider array
        dividerTitles = mutableListOf()
        for (i in 0 until NUMBER_OF_ROWS step 1) {
            dividerTitles.add(
                when (i) {
                    in 0 until 10 -> {
                        "0$i:00"
                    }
                    in 10 .. 24->{
                        "${i}:00"
                    }
                    else -> {
                        ""
                    }
                }
            )
        }


        if (set == null) {

            dividerPaint = Paint().apply {
                strokeWidth = 1f.toPx().toFloat()
                style = Paint.Style.FILL
                color = ContextCompat.getColor(context, dividerColorId)
            }

            textPaint = Paint().apply {
                color = ContextCompat.getColor(context, dividerTextColorId)
                textSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 20f,
                    resources.displayMetrics
                )
            }

            return
        }

        val typedArray = context.obtainStyledAttributes(set, R.styleable.CustomView)
        eachHourHeightInDp = typedArray.getDimension(R.styleable.CustomView_eachRowHeight, 118f)
        minimumHeightEachSellPercentage = typedArray.getFloat(R.styleable.CustomView_minimumPercentage, .25f)
        @SuppressLint("ResourceAsColor")
        dividerColorId = typedArray.getColor(R.styleable.CustomView_dividerColor, Color.BLACK)
        @SuppressLint("ResourceAsColor")
        dividerTextColorId = typedArray.getColor(R.styleable.CustomView_dividerTextColor, Color.BLACK)
        dividerStartOffset = typedArray.getDimension(R.styleable.CustomView_dividerStartOffset, 108f)

        if (!typedArray.getTextArray(R.styleable.CustomView_dividerTitles).isNullOrEmpty()) {
            dividerTitles = typedArray.getTextArray(R.styleable.CustomView_dividerTitles).map { it ->
                it.toString()
            }.toMutableList()
            if (dividerTitles.size < NUMBER_OF_ROWS)
                throw ArrayIndexOutOfBoundsException("Divider titles array size is smaller than number of rows")
        }
        typedArray.recycle()

        dividerPaint = Paint().apply {
            // divider styles and colors
            strokeWidth = 2f.toPx().toFloat()
            style = Paint.Style.FILL_AND_STROKE
            @SuppressLint("ResourceAsColor")
            color = dividerColorId
        }

        textPaint = Paint().apply {
            @SuppressLint("ResourceAsColor")
            // the color of hour text
            color = dividerTextColorId
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16f,
                resources.displayMetrics
            )
        }


    }



    private fun scrollToCurrentTime(time: Time){

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        post {
            if (childCount > 0)
                getChildAt(childCount - 1).visibility = View.VISIBLE
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            resources.displayMetrics.widthPixels.coerceAtLeast((maxChildrenEnd + dividerStartOffset.toPx())),
            eachHourHeightInDp.toPx() * NUMBER_OF_ROWS
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawDividersAndTimeCaption(it)
        }
    }

    private fun drawDividersAndTimeCaption(canvas: Canvas) {
        val eachHourHeightInPx = eachHourHeightInDp.toPx()
        val dividerInPx = dividerStartOffset.toPx().toFloat()

        for (i in 0 until NUMBER_OF_ROWS step 1) {
            val y = i * eachHourHeightInPx.toFloat()
            val rect = Rect()
            textPaint.getTextBounds(dividerTitles[i], 0, dividerTitles[i].length, rect)

            canvas.drawText(
                dividerTitles[i], (dividerInPx - rect.width()) / 2,
                y + eachHourHeightInPx / 2 + rect.height() / 2, textPaint
            )

            canvas.drawLine(0f, y, measuredWidth.toFloat(), y, dividerPaint)

        }
    }



    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        maxChildrenEnd = 0
        val dividerStartOffsetpx = dividerStartOffset.toPx()
        for (i in 0 until childCount) {
            @Suppress("UNCHECKED_CAST")
            val child = getChildAt(i) as EventView<Event>
            child.top = convertMinuteToPx(child.getEventTime().first)
            child.left = calculateNewChildPlaceLeft(i, child)
//            offset for divider applied
            child.left = if (child.left < dividerStartOffsetpx) {
                dividerStartOffsetpx
            } else {
                child.left
            }
            child.layout(
                child.left, child.top,
                child.left + child.measuredWidth,
                child.top + child.measuredHeight
            )

            if (child.right > maxChildrenEnd)
                maxChildrenEnd = child.right
        }
        post {
            requestLayout()
        }
    }

    private fun calculateNewChildPlaceLeft(index: Int, child: EventView<Event>): Int {
        for (i in 0 until index) {
            @Suppress("UNCHECKED_CAST")
            val childToCheckForSpace = getChildAt(i) as EventView<Event>

            //if there is no height collision we can skip to another child to check
            if (!hasCommonTime(child.getEventTime(), childToCheckForSpace.getEventTime())
                || child.left + child.measuredWidth < childToCheckForSpace.left
            )
                continue

            //shift right when detect width collision and check again for new changes
            if (child.left < childToCheckForSpace.right) {
                child.left = childToCheckForSpace.right
                calculateNewChildPlaceLeft(index, child)
            }
        }
        return child.left
    }


    private fun hasCommonTime(firstRange: Pair<Float, Float>, secondRange: Pair<Float, Float>): Boolean =
        !(firstRange.first >= secondRange.second || firstRange.second <= secondRange.first)


    private fun convertMinuteToPx(startTime: Float) = (startTime * eachHourHeightInDp.toPx()).roundToInt()


    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return CustomView.LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    /**
     * Custom per-child layout information.
     */
    class LayoutParams : ViewGroup.MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs)

        constructor(width: Int, height: Int) : super(width, height)

        constructor(source: ViewGroup.LayoutParams) : super(source)
    }

    private fun Float.toPx(): Int {
        val r = resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, r.displayMetrics).toInt()
    }


}