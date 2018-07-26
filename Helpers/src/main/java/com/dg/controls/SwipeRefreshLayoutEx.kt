package com.dg.controls

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet

class SwipeRefreshLayoutEx : SwipeRefreshLayout
{
    private var mMeasured = false
    private var mHasPostponedSetRefreshing = false
    private var mPreMeasureRefreshing = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // There's a bug where setRefreshing(...) before onMeasure() has no effect at all.
        if (!mMeasured)
        {
            mMeasured = true

            if (mHasPostponedSetRefreshing)
            {
                isRefreshing = mPreMeasureRefreshing
            }
        }
    }

    override fun setRefreshing(refreshing: Boolean)
    {
        if (mMeasured)
        {
            mHasPostponedSetRefreshing = false
            super.setRefreshing(refreshing)
        }
        else
        {
            // onMeasure was not called yet, so we postpone the setRefreshing until after onMeasure
            mHasPostponedSetRefreshing = true
            mPreMeasureRefreshing = refreshing
        }
    }
}
