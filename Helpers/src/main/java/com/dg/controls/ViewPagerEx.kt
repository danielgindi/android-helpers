package com.dg.controls

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

import com.dg.R

class ViewPagerEx : ViewPager
{
    @Suppress("MemberVisibilityCanBePrivate")
    internal var mSwipeEnabled = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {

        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ViewPager)
        try
        {
            mSwipeEnabled = styledAttributes.getBoolean(R.styleable.ViewPager_swipeEnabled, true)
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
