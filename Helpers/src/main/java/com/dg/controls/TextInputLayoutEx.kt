package com.dg.controls

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import com.dg.R
import com.dg.helpers.FontHelper

@Suppress("unused")
class TextInputLayoutEx : TextInputLayout
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        setCustomFontFamily(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context,
                attrs,
                defStyle)
    {
        setCustomFontFamily(context, attrs)
    }

    private fun setCustomFontFamily(context: Context, attrs: AttributeSet)
    {
        if (isInEditMode)
        {
            return
        }

        var fontFamily: String? = null
        var styledAttributes: TypedArray? = null
        try
        {
            styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TextInputLayoutEx)
            fontFamily = styledAttributes!!.getString(R.styleable.TextInputLayoutEx_customFontFamily)
        }
        finally
        {
            styledAttributes?.recycle()
        }

        if (fontFamily != null && !fontFamily.isEmpty())
        {
            setCustomFont(fontFamily)
        }
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
