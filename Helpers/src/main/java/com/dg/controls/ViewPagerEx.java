package com.dg.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dg.R;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */

public class ViewPagerEx extends ViewPager
{
    boolean mSwipeEnabled = true;

    public ViewPagerEx(Context context)
    {
        super(context);
    }

    public ViewPagerEx(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPager);
        try
        {
            mSwipeEnabled = a.getBoolean(R.styleable.ViewPager_swipeEnabled, true);
        }
        finally
        {
            a.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (mSwipeEnabled)
        {
            return super.onInterceptTouchEvent(event);
        }

        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mSwipeEnabled)
        {
            return super.onTouchEvent(event);
        }

        // Never allow swiping to switch between pages
        return false;
    }
}
