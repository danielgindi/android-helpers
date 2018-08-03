package com.dg.helpers

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.ScaleAnimation

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Code here was taken from Android's open source.
 * It can be used to create the appropriate transition animation for a fragment,
 * in case you had to override the default implementation for some reason.
 * (Android's default implementation is private access, so this gives public access).
 */
@Suppress("unused")
object FragmentTransitionHelper
{
    const val ANIM_STYLE_OPEN_ENTER = 1
    const val ANIM_STYLE_OPEN_EXIT = 2
    const val ANIM_STYLE_CLOSE_ENTER = 3
    const val ANIM_STYLE_CLOSE_EXIT = 4
    const val ANIM_STYLE_FADE_ENTER = 5
    const val ANIM_STYLE_FADE_EXIT = 6

    private val DECELERATE_QUINT = DecelerateInterpolator(2.5f)
    private val DECELERATE_CUBIC = DecelerateInterpolator(1.5f)

    private val ANIM_DUR = 220

    fun transitToStyleIndex(transit: Int, enter: Boolean): Int
    {
        var animAttr = -1
        when (transit)
        {
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> animAttr = if (enter) ANIM_STYLE_OPEN_ENTER else ANIM_STYLE_OPEN_EXIT
            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> animAttr = if (enter) ANIM_STYLE_CLOSE_ENTER else ANIM_STYLE_CLOSE_EXIT
            FragmentTransaction.TRANSIT_FRAGMENT_FADE -> animAttr = if (enter) ANIM_STYLE_FADE_ENTER else ANIM_STYLE_FADE_EXIT
        }
        return animAttr
    }

    private fun makeOpenCloseAnimation(startScale: Float,
                                       endScale: Float,
                                       startAlpha: Float,
                                       endAlpha: Float): Animation
    {
        val set = AnimationSet(false)
        val scale = ScaleAnimation(startScale, endScale, startScale, endScale,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
        scale.interpolator = DECELERATE_QUINT
        scale.duration = ANIM_DUR.toLong()
        set.addAnimation(scale)
        val alpha = AlphaAnimation(startAlpha, endAlpha)
        alpha.interpolator = DECELERATE_CUBIC
        alpha.duration = ANIM_DUR.toLong()
        set.addAnimation(alpha)
        return set
    }

    private fun makeFadeAnimation(start: Float, end: Float): Animation
    {
        val anim = AlphaAnimation(start, end)
        anim.interpolator = DECELERATE_CUBIC
        anim.duration = ANIM_DUR.toLong()
        return anim
    }

    fun animationForFragmentTransition(transit: Int, enter: Boolean): Animation?
    {
        if (transit == 0)
        {
            return null
        }

        val styleIndex = transitToStyleIndex(transit, enter)
        if (styleIndex < 0)
        {
            return null
        }

        when (styleIndex)
        {
            ANIM_STYLE_OPEN_ENTER -> return makeOpenCloseAnimation(1.125f, 1.0f, 0f, 1f)
            ANIM_STYLE_OPEN_EXIT -> return makeOpenCloseAnimation(1.0f, .975f, 1f, 0f)
            ANIM_STYLE_CLOSE_ENTER -> return makeOpenCloseAnimation(.975f, 1.0f, 0f, 1f)
            ANIM_STYLE_CLOSE_EXIT -> return makeOpenCloseAnimation(1.0f, 1.075f, 1f, 0f)
            ANIM_STYLE_FADE_ENTER -> return makeFadeAnimation(0f, 1f)
            ANIM_STYLE_FADE_EXIT -> return makeFadeAnimation(1f, 0f)
        }

        return null
    }

    fun defaultAnimationForFragmentTransition(
            fragmentManager: FragmentManager?,
            fragment: Fragment,
            transit: Int,
            enter: Boolean,
            nextAnim: Int): Animation?
    {
        var anim: Animation? = null

        if (nextAnim != 0)
        {
            anim = AnimationUtils.loadAnimation(fragment.context, nextAnim)
        }

        if (anim == null)
        {
            if (fragmentManager != null)
            {
                try
                {
                    val loadAnimation = fragmentManager.javaClass
                            .getMethod("loadAnimation",
                                    Fragment::class.java,
                                    Int::class.java,
                                    Boolean::class.java,
                                    Int::class.java)
                    anim = loadAnimation.invoke(fragment, transit, enter, 0) as? Animation
                }
                catch (ignore: NoSuchMethodException)
                {
                }
                catch (ignore: SecurityException)
                {
                }
                catch (ignore: InvocationTargetException)
                {
                }
                catch (ignore: IllegalAccessException)
                {
                }

            }
        }

        if (anim == null)
        {
            anim = animationForFragmentTransition(transit, enter)
        }

        return anim
    }
}
