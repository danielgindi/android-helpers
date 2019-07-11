package com.dg.animations

import android.widget.PopupWindow

@Suppress("unused")
class PopupWindowResizeWidthAnimation : PopupWindowResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param popupWindow - the popup to animate
     * @param fromWidth - the original width to animate from.
     * Use `CURRENT_SIZE` to set from current width automatically.
     * @param toWidth - the target width to animate to.
     * Use `CURRENT_SIZE` to set from current width automatically.
     */
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(popupWindow: PopupWindow,
                fromWidth: Int,
                toWidth: Int)
            : super(popupWindow,
            CURRENT_SIZE, CURRENT_SIZE,
            fromWidth, toWidth)
}