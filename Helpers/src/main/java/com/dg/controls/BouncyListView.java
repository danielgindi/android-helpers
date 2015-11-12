package com.dg.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class BouncyListView extends ListView {

    private Context context;
    private int maxYOverscrollDistance;

    public BouncyListView(Context context)
    {
        super(context);
        this.context = context;
        initBounceListView();
    }

    public BouncyListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        initBounceListView();
    }

    public BouncyListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        initBounceListView();
    }

    private void initBounceListView()
    {
        setMaxYOverscrollDistance(200);
    }

    public void setMaxYOverscrollDistance(int maxYOverscrollDistance)
    {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float density = metrics.density;

        this.maxYOverscrollDistance = (int) (density * maxYOverscrollDistance);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
    {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, this.maxYOverscrollDistance, isTouchEvent);
    }

}
