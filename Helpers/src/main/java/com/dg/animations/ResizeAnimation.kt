package com.dg.animations

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

open class ResizeAnimation : Animation
{

    private val mAnimatingView: View
    private var mFromWidth: Int
    private var mToWidth: Int
    private var mFromHeight: Int
    private var mToHeight: Int

    /**
     * Constructs a resize animation
     * @param view - the view to animate
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
    constructor(view: View,
                fromWidth: Int,
                toWidth: Int,
                fromHeight: Int,
                toHeight: Int) : super()
    {
        this.mAnimatingView = view
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
        val params = mAnimatingView.layoutParams

        if (mWidthEnabled)
        {
            if (mFromWidth == CURRENT_SIZE)
            {
                mFromWidth = mAnimatingView.measuredWidth
            }
            if (mToWidth == CURRENT_SIZE)
            {
                mToWidth = mAnimatingView.measuredWidth
            }

            params.width = (mFromWidth + (mToWidth - mFromWidth).toFloat() * interpolatedTime).toInt()
        }
        if (mHeightEnabled)
        {
            if (mFromHeight == CURRENT_SIZE)
            {
                mFromHeight = mAnimatingView.measuredHeight
            }
            if (mToHeight == CURRENT_SIZE)
            {
                mToHeight = mAnimatingView.measuredHeight
            }

            params.height = (mFromHeight + (mToHeight - mFromHeight).toFloat() * interpolatedTime).toInt()
        }

        mAnimatingView.requestLayout()
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