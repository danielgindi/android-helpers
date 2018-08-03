package com.dg.animations

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.PopupWindow

@Suppress("unused")
open class PopupWindowResizeAnimation : Animation
{
    private val mPopupWindow: PopupWindow
    private var mFromWidth: Int
    private var mToWidth: Int
    private var mFromHeight: Int
    private var mToHeight: Int

    /**
     * Constructs a resize animation
     * @param popupWindow - the popup to animate
     * @param fromWidth - the original width to animate from.
     * Use `CURRENT_SIZE` to set from current width automatically.
     * Set `fromWidth` and `toWidth` to `CURRENT_SIZE` to disable resizing this axis.
     * @param toWidth - the target width to animate to.
     * Use `CURRENT_SIZE` to set from current width automatically.
     * Set `fromWidth` and `toWidth` to `CURRENT_SIZE` to disable resizing this axis.
     * @param fromHeight - the original height to animate from.
     * Use `CURRENT_SIZE` to set from current height automatically.
     * Set `fromHeight` and `toHeight` to `CURRENT_SIZE` to disable resizing this axis.
     * @param toHeight - the target height to animate to.
     * Use `CURRENT_SIZE` to set from current height automatically.
     * Set `fromHeight` and `toHeight` to `CURRENT_SIZE` to disable resizing this axis.
     */
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(popupWindow: PopupWindow,
                fromWidth: Int,
                toWidth: Int,
                fromHeight: Int,
                toHeight: Int) : super()
    {
        this.mPopupWindow = popupWindow
        this.mFromWidth = fromWidth
        this.mToWidth = toWidth
        this.mFromHeight = fromHeight
        this.mToHeight = toHeight
        mWidthEnabled = fromWidth != CURRENT_SIZE || toWidth != CURRENT_SIZE
        mHeightEnabled = fromHeight != CURRENT_SIZE || toHeight != CURRENT_SIZE
    }

    private val mWidthEnabled: Boolean
    private val mHeightEnabled: Boolean

    override fun applyTransformation(interpolatedTime: Float, t: Transformation)
    {
        if (mWidthEnabled)
        {
            if (mFromWidth == CURRENT_SIZE)
            {
                mFromWidth = mPopupWindow.width
            }
            if (mToWidth == CURRENT_SIZE)
            {
                mToWidth = mPopupWindow.width
            }

            mPopupWindow.width = (mFromWidth + (mToWidth - mFromWidth).toFloat() * interpolatedTime).toInt()
        }
        if (mHeightEnabled)
        {
            if (mFromHeight == CURRENT_SIZE)
            {
                mFromHeight = mPopupWindow.height
            }
            if (mToHeight == CURRENT_SIZE)
            {
                mToHeight = mPopupWindow.height
            }

            mPopupWindow.height = (mFromHeight + (mToHeight - mFromHeight).toFloat() * interpolatedTime).toInt()
        }

        mPopupWindow.update(mPopupWindow.width, mPopupWindow.height)
    }

    override fun willChangeBounds(): Boolean
    {
        return true
    }

    companion object
    {
        const val CURRENT_SIZE = -1
    }
}