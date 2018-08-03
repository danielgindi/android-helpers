package com.dg.spannable

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * Changes the typeface family of the text to which the span is attached.
 */
@Suppress("unused")
class CustomTypefaceSpannable : MetricAffectingSpan
{
    @Suppress("MemberVisibilityCanBePrivate")
    val typeface: Typeface

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(typeface: Typeface) : super()
    {
        this.typeface = typeface
    }

    val spanTypeId: Int
        get() = SPANNABLE_ID

    fun describeContents(): Int
    {
        return 0
    }

    override fun updateDrawState(ds: TextPaint)
    {
        apply(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint)
    {
        apply(paint, typeface)
    }

    companion object
    {
        private const val SPANNABLE_ID = 100

        private fun apply(paint: Paint, typeface: Typeface)
        {
            paint.typeface = typeface
        }
    }
}