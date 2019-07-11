package com.dg.animations

import android.view.View

@Suppress("unused")
class ResizeHeightAnimation : ResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param view - the view to animate
     * @param fromHeight - the original height to animate from.
     * Use `CURRENT_SIZE` to set from current height automatically.
     * @param toHeight - the target height to animate to.
     * Use `CURRENT_SIZE` to set from current height automatically.
     */
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(view: View,
                fromHeight: Int,
                toHeight: Int) : super(view, CURRENT_SIZE, CURRENT_SIZE, fromHeight, toHeight)
}