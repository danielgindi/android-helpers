package com.dg.spannable;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 *
 * Changes the typeface family of the text to which the span is attached.
 */
public class CustomTypefaceSpannable extends MetricAffectingSpan
{
    private Typeface mTypeface;

    private static int SPANNABLE_ID = 100;

    public CustomTypefaceSpannable(Typeface typeface)
    {
        mTypeface = typeface;
    }

    public int getSpanTypeId()
    {
        return SPANNABLE_ID;
    }

    public int describeContents()
    {
        return 0;
    }

    public Typeface getTypeface()
    {
        return mTypeface;
    }

    @Override
    public void updateDrawState(TextPaint ds)
    {
        apply(ds, mTypeface);
    }

    @Override
    public void updateMeasureState(TextPaint paint)
    {
        apply(paint, mTypeface);
    }

    private static void apply(Paint paint, Typeface typeface)
    {
        paint.setTypeface(typeface);
    }
}