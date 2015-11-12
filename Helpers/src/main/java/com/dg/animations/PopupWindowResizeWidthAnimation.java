package com.dg.animations;

import android.widget.PopupWindow;

/**
 * Created by Daniel Cohen Gindi
 * danielgindi@gmail.com
 */

public class PopupWindowResizeWidthAnimation extends PopupWindowResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param popupWindow - the popup to animate
     * @param fromWidth - the original width to animate from.
     *                  Use `CURRENT_SIZE` to set from current width automatically.
     * @param toWidth - the target width to animate to.
     *                  Use `CURRENT_SIZE` to set from current width automatically.
     */
    public PopupWindowResizeWidthAnimation(PopupWindow popupWindow,
                                            int fromWidth, int toWidth)
    {
        super(popupWindow, CURRENT_SIZE, CURRENT_SIZE, fromWidth, toWidth);
    }
}