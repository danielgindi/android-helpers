package com.dg.controls

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

class BouncyListView : ListView
{
    private var maxYOverscrollDistance: Int = 0

    constructor(context: Context) : super(context)
    {
        initBounceListView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initBounceListView()
    }

    constructor(context: Context,
                attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle)
    {
        initBounceListView()
    }

    private fun initBounceListView()
    {
        setMaxYOverscrollDistance(200)
    }

    fun setMaxYOverscrollDistance(maxYOverscrollDistance: Int)
    {
        val metrics = context!!.resources.displayMetrics
        val density = metrics.density

        this.maxYOverscrollDistance = (density * maxYOverscrollDistance).toInt()
    }

    override fun overScrollBy(deltaX: Int,
                              deltaY: Int,
                              scrollX: Int,
                              scrollY: Int,
                              scrollRangeX: Int,
                              scrollRangeY: Int,
                              maxOverScrollX: Int,
                              maxOverScrollY: Int,
                              isTouchEvent: Boolean): Boolean
    {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, this.maxYOverscrollDistance, isTouchEvent)
    }

}
