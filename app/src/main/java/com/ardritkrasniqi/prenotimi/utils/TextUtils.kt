package com.ardritkrasniqi.prenotimi.utils

import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.widget.RelativeLayout
import android.widget.TextView

object TextUtils {
    fun getTextHeight(textView: TextView): Float {
        return StaticLayout(
            textView.text, textView.paint,
            getTextWidth(textView).toInt(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f,
            true
        ).height.toFloat()
    }

    private fun getTextWidth(textView: TextView): Float {
        val param =
            textView.layoutParams as RelativeLayout.LayoutParams
        val measureTextWidth =
            textView.paint.measureText(textView.text.toString())
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            (Math.max(measureTextWidth, param.width.toFloat())
                    + param.marginEnd
                    + param.marginStart)
        } else {
            measureTextWidth.coerceAtLeast(param.width.toFloat()) + param.leftMargin + param.rightMargin
        }
    }
}
