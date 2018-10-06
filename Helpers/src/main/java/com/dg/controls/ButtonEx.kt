package com.dg.controls

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.dg.R
import com.dg.helpers.FontHelper

@Suppress("unused")
class ButtonEx : AppCompatButton
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        setCustomFontFamily(context, attrs)
    }

    constructor(context: Context,
                attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle)
    {
        setCustomFontFamily(context, attrs)
    }

    private fun setCustomFontFamily(context: Context, attrs: AttributeSet)
    {
        if (isInEditMode)
        {
            return
        }

        val fontFamily: String?
        var styledAttributes: TypedArray? = null
        try
        {
            styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomFontFamily)
            fontFamily = styledAttributes!!.getString(R.styleable.CustomFontFamily_customFontFamily)
        }
        finally
        {
            styledAttributes?.recycle()
        }

        if (fontFamily != null && !fontFamily.isEmpty())
        {
            paintFlags = this.paintFlags or Paint.SUBPIXEL_TEXT_FLAG or Paint.LINEAR_TEXT_FLAG
            setCustomFont(fontFamily)
        }
    }

    @Deprecated("This version was a mistake", replaceWith = ReplaceWith("setCustomFont(fontFamily)"))
    fun setCustomFont(context: Context, fontFamily: String): Boolean
    {
        return setCustomFont(fontFamily)
    }

    fun setCustomFont(fontFamily: String): Boolean
    {
        val typeface: Typeface? = FontHelper.getFont(context, fontFamily)
        return if (typeface != null)
        {
            setTypeface(typeface)
            true
        }
        else
        {
            false
        }
    }
}
