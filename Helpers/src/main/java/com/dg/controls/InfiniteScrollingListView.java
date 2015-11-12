package com.dg.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class InfiniteScrollingListView extends ListView
{
    private boolean _mIsLoadingMore;
    private boolean _mSuspendInfiniteScroll;
    private ProgressBar _mProgressBar;
    private InfiniteScrollActionHandler _mInfiniteScrollActionHandler;
    private LinearLayout _mInfiniteScrollFooter;

    public InfiniteScrollingListView(Context context)
    {
        super(context);
    }

    public InfiniteScrollingListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public InfiniteScrollingListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void infiniteScrollLoadCompleted()
    {
        _mIsLoadingMore = false;
        if (_mProgressBar != null)
        {
            _mProgressBar.setVisibility(View.GONE);
        }
    }

    public void suspendInfiniteScroll(boolean suspend)
    {
        _mSuspendInfiniteScroll = suspend;
    }

    public void addInfiniteScrollingWithActionHandler(InfiniteScrollActionHandler infiniteScrollActionHandler)
    {
        _mIsLoadingMore = false;

        this._mInfiniteScrollActionHandler = infiniteScrollActionHandler;

        if (_mProgressBar == null)
        {
            _mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
            LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            _mProgressBar.setLayoutParams(progressBarParams);
            _mProgressBar.setPadding(6, 6, 6, 6);

            _mInfiniteScrollFooter = new LinearLayout(getContext());
            LayoutParams layoutParams = new LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            _mInfiniteScrollFooter.setGravity(Gravity.CENTER);
            _mInfiniteScrollFooter.setLayoutParams(layoutParams);
            _mInfiniteScrollFooter.addView(_mProgressBar);

            _mProgressBar.setVisibility(View.GONE);

            addFooterView(_mInfiniteScrollFooter);
        }

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (_mSuspendInfiniteScroll) return;

                boolean loadMore;
                loadMore = (0 != totalItemCount) && ((firstVisibleItem + visibleItemCount) >= (totalItemCount));

                if (false == _mIsLoadingMore && true == loadMore && null != InfiniteScrollingListView.this._mInfiniteScrollActionHandler)
                {
                    if (InfiniteScrollingListView.this._mInfiniteScrollActionHandler.infiniteScrollAction())
                    {
                        _mIsLoadingMore = true;
                        _mProgressBar.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    public void removeInfiniteScrolling()
    {
        if (_mInfiniteScrollFooter != null)
        {
            removeFooterView(_mInfiniteScrollFooter);
        }
        _mInfiniteScrollFooter = null;
        _mProgressBar = null;
        _mInfiniteScrollActionHandler = null;
    }

    public interface InfiniteScrollActionHandler
    {
        public boolean infiniteScrollAction();
    }
}
