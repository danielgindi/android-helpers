package com.dg.animations

import android.widget.PopupWindow

@Suppress("unused")
class PopupWindowResizeHeightAnimation : PopupWindowResizeAnimation
{
    /**
     * Constructs a resize animation
     * @param popupWindow - the popup to animate
     * @param fromHeight - the original height to animate from.
     * Use `CURRENT_SIZE` to set from current height automatically.
     * @param toHeight - the target height to animate to.
     * Use `CURRENT_SIZE` to set from current height automatically.
     */
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(popupWindow: PopupWindow,
                fromHeight: Int,
                toHeight: Int)
            : super(popupWindow,
            CURRENT_SIZE, CURRENT_SIZE,
            fromHeight, toHeight)
}