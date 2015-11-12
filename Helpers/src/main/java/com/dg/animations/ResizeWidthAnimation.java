package com.dg.animations;

import android.view.View;

/**
 * Created by Daniel Cohen Gindi
 * danielgindi@gmail.com
 */

public class ResizeWidthAnimation extends ResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param view - the view to animate
     * @param fromWidth - the original width to animate from.
     *                  Use `CURRENT_SIZE` to set from current width automatically.
     * @param toWidth - the target width to animate to.
     *                  Use `CURRENT_SIZE` to set from current width automatically.
     */
    public ResizeWidthAnimation(View view,
                                int fromWidth, int toWidth)
    {
        super(view, fromWidth, toWidth, CURRENT_SIZE, CURRENT_SIZE);
    }
}