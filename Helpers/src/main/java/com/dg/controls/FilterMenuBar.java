package com.dg.controls;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.dg.R;
import com.dg.animations.PopupWindowResizeHeightAnimation;
import com.dg.animations.ResizeHeightAnimation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel Cohen Gindi
 * danielgindi@gmail.com
 */

public class FilterMenuBar extends LinearLayout
{
    private FilterMenuBarListener mListener;
    private ArrayList<Item> mItems = new ArrayList<>();
    private Item mCurrentOpenFilter;
    private float mAutoCloseTimeoutSeconds = 3.f;
    private Handler mHandler;
    private Runnable mAutoHideRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            autoHide();
        }
    };

    private View mFilterDropDownView;
    private PopupWindow mFilterPopup;
    private boolean mDropDownAnimating;

    public FilterMenuBar(Context context)
    {
        super(context);
        setOrientation(HORIZONTAL);
        init(context);

    }

    public FilterMenuBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        mHandler = new Handler();
    }

    /**
     * @return the listener for the filter bar
     */
    public FilterMenuBarListener getFilterMenuBarListener()
    {
        return mListener;
    }

    /**
     * Sets a listener for the filter bar.
     * This listener is used to retrieve data and notify events.
     * @param listener
     */
    public void setFilterMenuBarListener(FilterMenuBarListener listener)
    {
        mListener = listener;
    }

    /**
     * Closes any open filters, and clears all data, removing all filters and buttons.
     */
    public void clear()
    {
        mItems.clear();

        if (mFilterPopup != null)
        {
            mFilterPopup.dismiss();
            mFilterPopup = null;
        }

        if (mFilterDropDownView != null)
        {
            if (mFilterDropDownView.getParent() != null)
            {
                ((ViewGroup)mFilterDropDownView.getParent()).removeView(mFilterDropDownView);
            }
            mFilterDropDownView = null;
        }

        mCurrentOpenFilter = null;

        suspendAutoHide();
    }

    /**
     * Adds a new filter to the bar.
     * The Button for the filter will be queried immediately through the listener.
     * Other data will be queried on demand.
     * @param key - the name of the filter to add
     */
    public void addItem(String key)
    {
        final Item item = new Item();

        item.setKey(key);

        FilterMenuBarListener listener = getFilterMenuBarListener();

        item.setButton(getFilterMenuBarListener().onButtonForFilter(this, key, this));

        if (item.getButton() == null)
        {
            return;
        }

        item.setOriginalMinWidth(item.getButton().getMinWidth());

        String defaultString = listener.onDefaultStringForFilter(this, key);
        if (defaultString != null)
        {
            item.getButton().setText(defaultString);
        }

        item.getButton().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickedForItem(item);
            }

        });

        mItems.add(item);
        addView(item.getButton());
    }

    private void onButtonClickedForItem(Item item)
    {
        if (mCurrentOpenFilter == item)
        {
            close(true);
        }
        else
        {
            openFilter(item.getKey(), true);
        }
    }

    /**
     * Shows the dropdown for the specified filter.
     * @param filter - the filter to show
     * @param animated - specifies whether the action should be animated or not.
     */
    public void openFilter(String filter, boolean animated)
    {
        if (mCurrentOpenFilter != null &&
                mCurrentOpenFilter.getKey().equals(filter))
            return;

        if (mCurrentOpenFilter != null)
        {
            close(false);
        }

        if (filter == null || mCurrentOpenFilter != null) return;

        Item barItem = null;
        for (Item item : mItems)
        {
            if (item.getKey().equals(filter))
            {
                barItem = item;
                break;
            }
        }

        FilterMenuBarListener listener = getFilterMenuBarListener();

        if (!listener.onFilterBarShouldOpenForFilter(this, filter))
        {
            return;
        }

        mCurrentOpenFilter = barItem;

        if (mCurrentOpenFilter == null) return;

        mFilterDropDownView = listener.onDropDownViewForFilterBar(this, mCurrentOpenFilter.getKey());
        ViewGroup.LayoutParams listViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFilterDropDownView.setLayoutParams(listViewParams);

        showDropDownForCurrentFilter(animated);

        resumeAutoHide();
    }

    private ViewGroup getTopView()
    {
        ViewGroup filterBarTopView = this;

        while (filterBarTopView != null)
        {
            if (filterBarTopView instanceof HorizontalScrollView && getOrientation() == HORIZONTAL) break;
            if (!(filterBarTopView instanceof HorizontalScrollView) &&
                    filterBarTopView instanceof ScrollView && getOrientation() == VERTICAL) break;

            filterBarTopView = (ViewGroup)filterBarTopView.getParent();
        }

        if (filterBarTopView == null)
        {
            filterBarTopView = this;
        }

        return filterBarTopView;
    }

    private void showDropDownForCurrentFilter(boolean animated)
    {
        ViewGroup filterBarTopView = getTopView();

        ViewGroup parentView = (ViewGroup)filterBarTopView.getParent();

        if ((parentView instanceof RelativeLayout && filterBarTopView.getId() != NO_ID) ||
                parentView instanceof FrameLayout)
        {
            parentView.addView(mFilterDropDownView);
        }
        else
        {
            // We can't use a simple view, make up a PopupView, as windows have absolute positions.
            // Problem with this approach is that it is on top of just about everything, including side drawers...

            mFilterPopup = new PopupWindow(mFilterDropDownView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

            mFilterPopup.setTouchable(true);
            mFilterPopup.setOutsideTouchable(true);
            mFilterPopup.setFocusable(false);
        }

        recalculateDropdownSize(false);

        if (mFilterPopup != null)
        {
            if (animated)
            {
                mFilterPopup.setAnimationStyle(R.style.FilterBarAnimation);
            }
            else
            {
                mFilterPopup.setAnimationStyle(android.R.style.Animation);
            }

            int[] location = new int[2];
            filterBarTopView.getLocationInWindow(location);

            int popupY = location[1] + filterBarTopView.getMeasuredHeight();

            mFilterPopup.showAtLocation((View) filterBarTopView.getParent(),
                                        Gravity.TOP,
                                        location[0],
                                        popupY);
        }
        else
        {
            if (animated)
            {
                final View viewToAnimate = mFilterDropDownView;

                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down_in);
                animation.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        animation.cancel();
                        if (viewToAnimate == mFilterDropDownView)
                        {
                            mDropDownAnimating = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });

                viewToAnimate.startAnimation(animation);
                mDropDownAnimating = true;
            }
        }
    }

    private ListView findChildListView(ViewGroup parent)
    {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++)
        {
            View child = parent.getChildAt(i);
            if (child instanceof ListView)
            {
                return (ListView)child;
            }
            if (child instanceof ViewGroup)
            {
                ListView listView = findChildListView((ViewGroup) child);
                if (listView != null)
                {
                    return listView;
                }
            }
        }

        return null;
    }

    private int getEstimatedSizeForDropdownView(View dropdownView, int parentWidth, int maxHeight)
    {
        int estimatedSize = ViewGroup.LayoutParams.WRAP_CONTENT;

        ListView listView = findChildListView((ViewGroup) dropdownView);
        if (listView != null)
        {
            int count = listView.getAdapter().getCount();
            if (count > 0)
            {
                dropdownView.measure(
                        MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
                );

                View testView = listView.getAdapter().getView(0, null, null);
                testView.measure(
                        MeasureSpec.makeMeasureSpec(listView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
                );

                estimatedSize = (testView.getMeasuredHeight() * count) +
                        listView.getDividerHeight() * count;
            }
            else
            {
                if (listView.getParent() instanceof SwipeRefreshLayout)
                {
                    estimatedSize = ((SwipeRefreshLayout)listView.getParent()).getProgressCircleDiameter() +
                            (int)(110 * getResources().getDisplayMetrics().density);
                    estimatedSize = Math.min(estimatedSize, maxHeight);
                }
            }
        }
        else
        {
            dropdownView.measure(
                    MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
            );
            if (dropdownView.getMeasuredHeight() > 0)
            {
                estimatedSize = dropdownView.getMeasuredHeight();
            }
        }

        return estimatedSize;
    }

    /**
     * Triggers a recalculation of the dropdown size.
     * Call this after your data has loaded and the list was populated.
     * @param animated - specifies whether the action should be animated or not.
     */
    public void recalculateDropdownSize(boolean animated)
    {
        ViewGroup filterBarTopView = getTopView();

        ViewGroup parentView = (ViewGroup)filterBarTopView.getParent();

        if (mFilterPopup != null)
        {
            // If we're not animating already, and we do have a parent,
            // Then it means we need to animate a scale change
            animated = animated &&
                    mFilterPopup.isShowing() &&
                    !mDropDownAnimating;

            int[] location = new int[2];
            final Rect displayFrame = new Rect();

            filterBarTopView.getLocationInWindow(location);
            filterBarTopView.getWindowVisibleDisplayFrame(displayFrame);

            // Determine dropdown's Y position
            int dropdownY = location[1] + filterBarTopView.getMeasuredHeight();

            int maxPopupHeight = displayFrame.bottom - dropdownY;

            int estimatedSize = getEstimatedSizeForDropdownView(mFilterPopup.getContentView(),
                                                                displayFrame.width(),
                                                                displayFrame.height());

            estimatedSize = Math.min(Math.max(estimatedSize < 15 ? maxPopupHeight : estimatedSize, estimatedSize), maxPopupHeight);

            mFilterPopup.setWidth(displayFrame.width());

            if (animated)
            {
                final PopupWindow popupToAnimate = mFilterPopup;

                PopupWindowResizeHeightAnimation animation = new PopupWindowResizeHeightAnimation(
                        mFilterPopup,
                        ResizeHeightAnimation.CURRENT_SIZE, estimatedSize);

                animation.setDuration(150);

                animation.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        if (popupToAnimate == mFilterPopup)
                        {
                            mDropDownAnimating = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });

                startAnimation(animation);
                mDropDownAnimating = true;
            }
            else
            {
                mFilterPopup.setHeight(estimatedSize);
            }
        }
        else if (mFilterDropDownView != null)
        {
            // If we're not animating already, and we do have a parent,
            // Then it means we need to animate a scale change
            animated = animated &&
                    mFilterDropDownView.getParent() != null &&
                    !mDropDownAnimating;

            // Determine dropdown's Y position
            int dropdownY = filterBarTopView.getTop() + filterBarTopView.getMeasuredHeight();
            int maxPopupHeight = parentView.getMeasuredHeight() - dropdownY;

            int estimatedSize = getEstimatedSizeForDropdownView(mFilterDropDownView,
                                                                parentView.getMeasuredWidth(),
                                                                maxPopupHeight);

            estimatedSize = estimatedSize < 0 ? estimatedSize :
                    Math.min(Math.max(estimatedSize < 15 ? maxPopupHeight : estimatedSize, estimatedSize), maxPopupHeight);

            ViewGroup.LayoutParams originalParams = mFilterDropDownView.getLayoutParams();
            ViewGroup.LayoutParams params = null;

            if (parentView instanceof RelativeLayout)
            {
                RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (animated && originalParams != null) ? originalParams.height : estimatedSize);
                relParams.addRule(RelativeLayout.BELOW, filterBarTopView.getId());
                params = relParams;
            }
            else if (parentView instanceof FrameLayout)
            {
                FrameLayout.LayoutParams frmParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (animated && originalParams != null) ? originalParams.height : estimatedSize);
                params = frmParams;
                parentView.setTop(dropdownY);
            }

            mFilterDropDownView.setLayoutParams(params);

            if (animated)
            {
                final View viewToAnimate = mFilterDropDownView;

                ResizeHeightAnimation animation = new ResizeHeightAnimation(
                        viewToAnimate,
                        ResizeHeightAnimation.CURRENT_SIZE, estimatedSize);

                animation.setDuration(150);

                animation.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        if (viewToAnimate == mFilterDropDownView)
                        {
                            mDropDownAnimating = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });

                viewToAnimate.startAnimation(animation);
                mDropDownAnimating = true;
            }
        }
    }

    /**
     * Closes the dropdown of the current filter.
     * @param animated - specifies whether the action should be animated or not.
     */
    public void close(boolean animated)
    {
        if (mCurrentOpenFilter == null) return;

        if (getFilterMenuBarListener() != null &&
                !getFilterMenuBarListener().onFilterBarShouldCloseForFilter(this, mCurrentOpenFilter.getKey()))
        {
            return;
        }

        suspendAutoHide();

        mCurrentOpenFilter = null;
        mDropDownAnimating = false;

        if (animated)
        {
            if (mFilterPopup != null)
            {
                mFilterPopup.setAnimationStyle(R.style.FilterBarAnimation);
            }
            else
            {
                final View viewToAnimate = mFilterDropDownView;

                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up_out);
                animation.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        if (viewToAnimate.getParent() != null)
                        {
                            ((ViewGroup) viewToAnimate.getParent()).removeView(viewToAnimate);
                        }
                        animation.cancel();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });

                viewToAnimate.startAnimation(animation);
            }
        }
        else
        {
            if (mFilterPopup != null)
            {
                mFilterPopup.setAnimationStyle(android.R.style.Animation);
            }
            else
            {
                ((ViewGroup)mFilterDropDownView.getParent()).removeView(mFilterDropDownView);
            }
        }

        if (mFilterPopup != null)
        {
            mFilterPopup.dismiss();
        }

        mFilterDropDownView = null;
        mFilterPopup = null;
    }

    /**
     * @param filter - the filter to fetch the value from
     * @return the current value of the specified filter
     */
    public Object getFilterValue(String filter)
    {
        for (Item item : mItems)
        {
            if (item.getKey().equals(filter))
            {
                return item.getValue();
            }
        }
        return null;
    }

    /**
     * Sets the selected value for the specified filter
     * @param filter - the filter to modify
     * @param value - the new value (or `null` for not selected)
     * @param title - the new title for the button. Ignored if null.
     * @param close - should the menu be closed?
     */
    public void setFilterSelectedValue(String filter, Object value, String title, boolean close)
    {
        for (Item item : mItems)
        {
            if (item.getKey().equals(filter))
            {
                item.setValue(value);

                if (title != null)
                {
                    item.getButton().setText(title);
                }

                break;
            }
        }

        if (close)
        {
            close(false);
        }
    }

    /**
     * Disables auto-hide temporarily.
     * Call this when detecting scroll events on a child-view of the dropdown, etc.
     * Call resumeAutoHide() when detecting no activity (like scroll-end)
     */
    public void suspendAutoHide()
    {
        mHandler.removeCallbacks(mAutoHideRunnable);
    }

    /**
     * Takes the auto-hide feature back on track
     */
    public void resumeAutoHide()
    {
        mHandler.removeCallbacks(mAutoHideRunnable);
        if (getAutoCloseTimeoutSeconds() > 0.f)
        {
            mHandler.postDelayed(mAutoHideRunnable, (int) (getAutoCloseTimeoutSeconds() * 1000f));
        }
    }

    private void autoHide()
    {
        boolean autoHide = true;

        if (getFilterMenuBarListener() != null)
        {
            autoHide = getFilterMenuBarListener().onFilterBarWillAutoHide(this, 0.15f);
        }

        if (autoHide)
        {
            close(true);
        }
    }

    /**
     * @return the key of the currently open filter
     */
    public String getCurrentOpenFilter()
    {
        return mCurrentOpenFilter != null ? mCurrentOpenFilter.getKey() : null;
    }

    /**
     * @return the supplied dropdown view of the currently open filter
     */
    public View getCurrentOpenFilterView()
    {
        return mFilterDropDownView;
    }

    /**
     * @return the amount of time to pass with no activity until auto-hide is triggered.
     */
    public float getAutoCloseTimeoutSeconds()
    {
        return mAutoCloseTimeoutSeconds;
    }

    /**
     * Sets the amount of time to pass with no activity until auto-hide is triggered.
     * @param autoCloseTimeoutSeconds - timeout in seconds, or 0 to disable
     */
    public void setAutoCloseTimeoutSeconds(float autoCloseTimeoutSeconds)
    {
        this.mAutoCloseTimeoutSeconds = autoCloseTimeoutSeconds;
    }

    /**
     * Get selected filter values
     * @return Map of filter keys to their values
     */
    public HashMap<String, Object> getSelectedValues()
    {
        HashMap<String, Object> map = new HashMap<>();

        for (Item item : mItems)
        {
            if (item.getValue() == null) continue;

            map.put(item.getKey(), item.getValue());
        }

        return map;
    }

    /**
     * This function tries to spread the buttons to fill the filter bar,
     * In case that they are not filling them already.
     * This works even when FilterMenuBar is inside a scrollview
     */
    public void spreadButtons()
    {
        View parentView = getTopView();

        int totalWidth = 0;
        int totalButtons = 0;

        for (Item item : mItems)
        {
            Button button = item.getButton();

            if (button == null) continue;

            totalButtons++;

            button.setMinimumWidth(item.getOriginalMinWidth());
            totalWidth += button.getMeasuredWidth();
        }

        int availableWidth = parentView.getWidth() - totalWidth;

        if (totalButtons > 0)
        {
            int extraWidth = Math.max(0, availableWidth / totalButtons);

            for (Item item : mItems)
            {
                Button button = item.getButton();

                if (button == null) continue;

                button.setMinimumWidth(button.getMeasuredWidth() + extraWidth);
            }
        }
    }

    private static class Item
    {
        private String mKey;
        private int mOriginalMinWidth;
        private Button mButton;
        private Object mValue;

        public String getKey()
        {
            return mKey;
        }

        public void setKey(String key)
        {
            this.mKey = key;
        }

        public int getOriginalMinWidth()
        {
            return mOriginalMinWidth;
        }

        public void setOriginalMinWidth(int minWidth)
        {
            this.mOriginalMinWidth = minWidth;
        }

        public Button getButton()
        {
            return mButton;
        }

        public void setButton(Button button)
        {
            this.mButton = button;
        }

        public Object getValue()
        {
            return mValue;
        }

        public void setValue(Object value)
        {
            this.mValue = value;
        }
    }

    public interface FilterMenuBarListener
    {
        Button onButtonForFilter(FilterMenuBar filterBar, String filter, ViewGroup container);

        boolean onFilterBarShouldOpenForFilter(FilterMenuBar filterBar, String filter);

        boolean onFilterBarShouldCloseForFilter(FilterMenuBar filterBar, String filter);

        View onDropDownViewForFilterBar(FilterMenuBar filterBar, String filter);

        String onDefaultStringForFilter(FilterMenuBar filterBar, String filter);

        boolean onFilterBarWillAutoHide(FilterMenuBar filterBar, float durationSeconds);
    }
}