package com.dg.animations;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.PopupWindow;

/**
 * Created by Daniel Cohen Gindi
 * danielgindi@gmail.com
 */

public class PopupWindowResizeAnimation extends Animation
{
    public static final int CURRENT_SIZE = -1;

    private PopupWindow mAnimatingPopup;

    private int mToWidth;
    private int mFromWidth;

    private int mToHeight;
    private int mFromHeight;

    private boolean mWidthEnabled;
    private boolean mHeightEnabled;

    /**
     * Constructs a resize animation
     * @param popupWindow - the popup to animate
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
    public PopupWindowResizeAnimation(PopupWindow popupWindow,
                                      int fromWidth, int toWidth,
                                      int fromHeight, int toHeight)
    {
        mAnimatingPopup = popupWindow;

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
        if (mWidthEnabled)
        {
            if (mFromWidth == CURRENT_SIZE)
            {
                mFromWidth = mAnimatingPopup.getWidth();
            }
            if (mToWidth == CURRENT_SIZE)
            {
                mToWidth = mAnimatingPopup.getWidth();
            }

            mAnimatingPopup.setWidth((int) (mFromWidth +
                    (float) (mToWidth - mFromWidth) * interpolatedTime));
        }
        if (mHeightEnabled)
        {
            if (mFromHeight == CURRENT_SIZE)
            {
                mFromHeight = mAnimatingPopup.getHeight();
            }
            if (mToHeight == CURRENT_SIZE)
            {
                mToHeight = mAnimatingPopup.getHeight();
            }

            mAnimatingPopup.setHeight((int) (mFromHeight +
                    (float) (mToHeight - mFromHeight) * interpolatedTime));
        }

        mAnimatingPopup.update(mAnimatingPopup.getWidth(), mAnimatingPopup.getHeight());
    }

    @Override
    public boolean willChangeBounds()
    {
        return true;
    }
}