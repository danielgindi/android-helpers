package com.dg.controls

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.dg.R

@Suppress("unused")
class ViewPagerEx : ViewPager
{
    @Suppress("MemberVisibilityCanBePrivate")
    internal var mSwipeEnabled = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {

        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerEx)
        try
        {
            mSwipeEnabled = styledAttributes.getBoolean(R.styleable.ViewPagerEx_swipeEnabled, true)
        }
        finally
        {
            styledAttributes.recycle()
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean
    {
        return if (mSwipeEnabled)
        {
            super.onInterceptTouchEvent(event)
        }
        else false

        // Never allow swiping to switch between pages
    }

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        return if (mSwipeEnabled)
        {
            super.onTouchEvent(event)
        }
        else false

        // Never allow swiping to switch between pages
    }
}
