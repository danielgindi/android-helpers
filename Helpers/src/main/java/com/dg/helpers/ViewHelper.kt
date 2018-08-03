package com.dg.helpers

import android.view.View

@Suppress("unused")
object ViewHelper
{
    /**
     * Force a measure and re-layout of children at this instant.
     * This is useful when "requestLayout" is too late and causes visual "jumps".
     *
     * Note: If a view's width/height are 0, then you probably need to re-layout its *parent*.
     * @param view
     */
    fun layoutNow(view: View)
    {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(view.measuredWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.measuredHeight, View.MeasureSpec.EXACTLY))
        view.layout(view.left,
                view.top,
                view.right,
                view.bottom)
    }
}
