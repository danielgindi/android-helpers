package com.dg.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Daniel Cohen Gindi
 * danielgindi@gmail.com
 */

public class ResizeAnimation extends Animation
{
    public static final int CURRENT_SIZE = -1;

    private View mAnimatingView;

    private int mToWidth;
    private int mFromWidth;

    private int mToHeight;
    private int mFromHeight;

    private boolean mWidthEnabled;
    private boolean mHeightEnabled;

    /**
     * Constructs a resize animation
     * @param view - the view to animate
     * @param fromWidth - the original width to animate from.
     *                  Use `CURRENT_SIZE` to set from current width automatically.
     *                  Set `fromWidth` and `toWidth` to `CURRENT_SIZE` to disable resizing this axis.
     * @param toWidth - the target width to animate to.
     *                  Use `CURRENT_SIZE` to set from current width automatically.
     *                  Set `fromWidth` and `toWidth` to `CURRENT_SIZE` to disable resizing this axis.
     * @param fromHeight - the original height to animate from.
     *                  Use `CURRENT_SIZE` to set from current height automatically.
     *                  Set `fromHeight` and `toHeight` to `CURRENT_SIZE` to disable resizing this axis.
     * @param toHeight - the target height to animate to.
     *                  Use `CURRENT_SIZE` to set from current height automatically.
     *                  Set `fromHeight` and `toHeight` to `CURRENT_SIZE` to disable resizing this axis.
     */
    public ResizeAnimation(View view,
                           int fromWidth, int toWidth,
                           int fromHeight, int toHeight)
    {
        mAnimatingView = view;

        mFromWidth = fromWidth;
        mToWidth = toWidth;
        mWidthEnabled = mFromWidth != CURRENT_SIZE || mToWidth != CURRENT_SIZE;

        mFromHeight = fromHeight;
        mToHeight = toHeight;
        mHeightEnabled = mFromHeight != CURRENT_SIZE || mToHeight != CURRENT_SIZE;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        ViewGroup.LayoutParams params = mAnimatingView.getLayoutParams();

        if (mWidthEnabled)
        {
            if (mFromWidth == CURRENT_SIZE)
            {
                mFromWidth = mAnimatingView.getMeasuredWidth();
            }
            if (mToWidth == CURRENT_SIZE)
            {
                mToWidth = mAnimatingView.getMeasuredWidth();
            }

            params.width = (int) (mFromWidth +
                    (float) (mToWidth - mFromWidth) * interpolatedTime);
        }
        if (mHeightEnabled)
        {
            if (mFromHeight == CURRENT_SIZE)
            {
                mFromHeight = mAnimatingView.getMeasuredHeight();
            }
            if (mToHeight == CURRENT_SIZE)
            {
                mToHeight = mAnimatingView.getMeasuredHeight();
            }

            params.height = (int) (mFromHeight +
                    (float) (mToHeight - mFromHeight) * interpolatedTime);
        }

        mAnimatingView.requestLayout();
    }

    @Override
    public boolean willChangeBounds()
    {
        return true;
    }
}