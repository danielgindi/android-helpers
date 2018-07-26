package com.dg.animations

import android.view.View

class ResizeWidthAnimation : ResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param view - the view to animate
     * @param fromWidth - the original width to animate from.
     * Use `CURRENT_SIZE` to set from current width automatically.
     * @param toWidth - the target width to animate to.
     * Use `CURRENT_SIZE` to set from current width automatically.
     */
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(view: View,
                fromWidth: Int,
                toWidth: Int) : super(view, fromWidth, toWidth, ResizeAnimation.CURRENT_SIZE, ResizeAnimation.CURRENT_SIZE)
}