package com.dg.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Code here was taken from Android's open source.
 * It can be used to create the appropriate transition animation for a fragment,
 *   in case you had to override the default implementation for some reason.
 * (Android's default implementation is private access, so this gives public access).
 */
public final class FragmentTransitionHelper
{
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;

    public static int transitToStyleIndex(int transit, boolean enter)
    {
        int animAttr = -1;
        switch (transit)
        {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                animAttr = enter ? ANIM_STYLE_OPEN_ENTER : ANIM_STYLE_OPEN_EXIT;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                animAttr = enter ? ANIM_STYLE_CLOSE_ENTER : ANIM_STYLE_CLOSE_EXIT;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                animAttr = enter ? ANIM_STYLE_FADE_ENTER : ANIM_STYLE_FADE_EXIT;
                break;
        }
        return animAttr;
    }

    private static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    private static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);

    private static final int ANIM_DUR = 220;

    private static Animation makeOpenCloseAnimation(float startScale,
                                            float endScale, float startAlpha, float endAlpha)
    {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(ANIM_DUR);
        set.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(ANIM_DUR);
        set.addAnimation(alpha);
        return set;
    }

    private static Animation makeFadeAnimation(float start, float end)
    {
        AlphaAnimation anim = new AlphaAnimation(start, end);
        anim.setInterpolator(DECELERATE_CUBIC);
        anim.setDuration(ANIM_DUR);
        return anim;
    }

    public static Animation animationForFragmentTransition(int transit, boolean enter)
    {
        if (transit == 0)
        {
            return null;
        }

        int styleIndex = transitToStyleIndex(transit, enter);
        if (styleIndex < 0)
        {
            return null;
        }

        switch (styleIndex)
        {
            case ANIM_STYLE_OPEN_ENTER:
                return makeOpenCloseAnimation(1.125f, 1.0f, 0, 1);
            case ANIM_STYLE_OPEN_EXIT:
                return makeOpenCloseAnimation(1.0f, .975f, 1, 0);
            case ANIM_STYLE_CLOSE_ENTER:
                return makeOpenCloseAnimation(.975f, 1.0f, 0, 1);
            case ANIM_STYLE_CLOSE_EXIT:
                return makeOpenCloseAnimation(1.0f, 1.075f, 1, 0);
            case ANIM_STYLE_FADE_ENTER:
                return makeFadeAnimation(0, 1);
            case ANIM_STYLE_FADE_EXIT:
                return makeFadeAnimation(1, 0);
        }

        return null;
    }

    public static Animation defaultAnimationForFragmentTransition(
            FragmentManager fragmentManager,
            Fragment fragment,
            int transit,
            boolean enter,
            int nextAnim)
    {
        Animation anim = null;

        if (nextAnim != 0)
        {
            anim = AnimationUtils.loadAnimation(fragment.getContext(), nextAnim);
        }

        if (anim == null)
        {
            if (fragmentManager != null)
            {
                try
                {
                    Method loadAnimation = fragmentManager.getClass()
                            .getMethod("loadAnimation",
                                    Fragment.class,
                                    Integer.class,
                                    Boolean.class,
                                    Integer.class);
                    anim = (Animation) loadAnimation.invoke(fragment, transit, enter, 0);
                }
                catch (NoSuchMethodException |
                        SecurityException |
                        InvocationTargetException |
                        IllegalAccessException ignore)
                {
                }
            }
        }

        if (anim == null)
        {
            anim = animationForFragmentTransition(transit, enter);
        }

        return anim;
    }
}
