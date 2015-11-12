package com.dg.animations;

import android.widget.PopupWindow;

/**
 * Created by Daniel Cohen Gindi
 * danielgindi@gmail.com
 */

public class PopupWindowResizeHeightAnimation extends PopupWindowResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param popupWindow - the popup to animate
     * @param fromHeight - the original height to animate from.
     *                  Use `CURRENT_SIZE` to set from current height automatically.
     * @param toHeight - the target height to animate to.
     *                  Use `CURRENT_SIZE` to set from current height automatically.
     */
    public PopupWindowResizeHeightAnimation(PopupWindow popupWindow,
                                            int fromHeight, int toHeight)
    {
        super(popupWindow, CURRENT_SIZE, CURRENT_SIZE, fromHeight, toHeight);
    }
}