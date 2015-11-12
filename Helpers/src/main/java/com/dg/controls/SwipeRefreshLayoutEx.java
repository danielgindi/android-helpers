package com.dg.controls;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */

public class SwipeRefreshLayoutEx extends SwipeRefreshLayout
{
    public SwipeRefreshLayoutEx(Context context)
    {
        super(context);
    }

    public SwipeRefreshLayoutEx(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private boolean mMeasured = false;
    private boolean mHasPosponedSetRefreshing = false;
    private boolean mPreMeasureRefreshing = false;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // There's a bug where setRefreshing(...) before onMeasure() has no effect at all.
        if (!mMeasured)
        {
            mMeasured = true;

            if (mHasPosponedSetRefreshing)
            {
                setRefreshing(mPreMeasureRefreshing);
            }
        }
    }


    @Override
    public void setRefreshing(boolean refreshing)
    {
        if (mMeasured)
        {
            mHasPosponedSetRefreshing = false;
            super.setRefreshing(refreshing);
        }
        else
        {
            // onMeasure was not called yet, so we postpone the setRefreshing until after onMeasure
            mHasPosponedSetRefreshing = true;
            mPreMeasureRefreshing = refreshing;
        }
    }
}
