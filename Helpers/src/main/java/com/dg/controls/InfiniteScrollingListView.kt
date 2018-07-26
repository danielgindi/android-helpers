package com.dg.controls

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar

class InfiniteScrollingListView : ListView
{
    private var mIsLoadingMore: Boolean = false
    private var mSuspendInfiniteScroll: Boolean = false
    private var mProgressBar: ProgressBar? = null
    private var mInfiniteScrollActionHandler: InfiniteScrollActionHandler? = null
    private var mInfiniteScrollFooter: LinearLayout? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context,
                attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle)

    fun infiniteScrollLoadCompleted()
    {
        mIsLoadingMore = false
        if (mProgressBar != null)
        {
            mProgressBar!!.visibility = View.GONE
        }
    }

    fun suspendInfiniteScroll(suspend: Boolean)
    {
        mSuspendInfiniteScroll = suspend
    }

    fun addInfiniteScrollingWithActionHandler(infiniteScrollActionHandler: InfiniteScrollActionHandler)
    {
        mIsLoadingMore = false

        this.mInfiniteScrollActionHandler = infiniteScrollActionHandler

        if (mProgressBar == null)
        {
            mProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleSmall)
            val progressBarParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            mProgressBar!!.layoutParams = progressBarParams
            mProgressBar!!.setPadding(6, 6, 6, 6)

            mInfiniteScrollFooter = LinearLayout(context)
            val layoutParams = AbsListView.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            mInfiniteScrollFooter!!.gravity = Gravity.CENTER
            mInfiniteScrollFooter!!.layoutParams = layoutParams
            mInfiniteScrollFooter!!.addView(mProgressBar)

            mProgressBar!!.visibility = View.GONE

            addFooterView(mInfiniteScrollFooter)
        }

        setOnScrollListener(object : AbsListView.OnScrollListener
        {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int)
            {

            }

            override fun onScroll(view: AbsListView,
                                  firstVisibleItem: Int,
                                  visibleItemCount: Int,
                                  totalItemCount: Int)
            {

                if (mSuspendInfiniteScroll) return

                val loadMore: Boolean = 0 != totalItemCount && firstVisibleItem + visibleItemCount >= totalItemCount

                if (!mIsLoadingMore && loadMore && null != this@InfiniteScrollingListView.mInfiniteScrollActionHandler)
                {
                    if (this@InfiniteScrollingListView.mInfiniteScrollActionHandler!!.infiniteScrollAction())
                    {
                        mIsLoadingMore = true
                        mProgressBar!!.visibility = View.VISIBLE
                    }
                }

            }
        })
    }

    fun removeInfiniteScrolling()
    {
        if (mInfiniteScrollFooter != null)
        {
            removeFooterView(mInfiniteScrollFooter)
        }
        mInfiniteScrollFooter = null
        mProgressBar = null
        mInfiniteScrollActionHandler = null
    }

    interface InfiniteScrollActionHandler
    {
        fun infiniteScrollAction(): Boolean
    }
}
