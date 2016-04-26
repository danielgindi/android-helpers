package com.dg.helpers;

import android.view.View;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class ViewHelper
{
    /**
     * Force a measure and re-layout of children at this instant.
     * This is useful when "requestLayout" is too late and causes visual "jumps".
     *
     * Note: If a view's width/height are 0, then you probably need to re-layout its *parent*.
     * @param view
     */
    public static void layoutNow(View view)
    {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        view.layout(view.getLeft(),
                    view.getTop(),
                    view.getRight(),
                    view.getBottom());
    }
}
