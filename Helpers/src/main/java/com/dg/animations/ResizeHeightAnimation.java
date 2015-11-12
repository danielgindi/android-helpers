package com.dg.animations;

import android.view.View;

/**
 * Created by Daniel Cohen Gindi
 * danielgindi@gmail.com
 */

public class ResizeHeightAnimation extends ResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param view - the view to animate
     * @param fromHeight - the original height to animate from.
     *                  Use `CURRENT_SIZE` to set from current height automatically.
     * @param toHeight - the target height to animate to.
     *                  Use `CURRENT_SIZE` to set from current height automatically.
     */
    public ResizeHeightAnimation(View view,
                                 int fromHeight, int toHeight)
    {
        super(view, CURRENT_SIZE, CURRENT_SIZE, fromHeight, toHeight);
    }
}