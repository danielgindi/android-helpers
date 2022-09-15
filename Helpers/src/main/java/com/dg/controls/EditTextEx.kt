package com.dg.controls

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.appcompat.widget.AppCompatEditText
import com.dg.R
import com.dg.helpers.FontHelper

@Suppress("unused")
class EditTextEx : AppCompatEditText
{
    private val mCanFocusZeroSized = Build.VERSION.SDK_INT < Build.VERSION_CODES.P

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
            styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.EditTextEx)
            fontFamily = styledAttributes!!.getString(R.styleable.EditTextEx_customFontFamily)
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

    /*
     * This is a workaround for a bug in Android where onKeyUp throws if requestFocus() returns false
     */
    override fun focusSearch(direction: Int): View?
    {
        val view = super.focusSearch(direction) ?: return null

        if (view.visibility != View.VISIBLE)
            return null
        if (!view.isFocusable)
            return null
        if (!view.isEnabled)
            return null
        if (!mCanFocusZeroSized && (view.bottom <= view.top || view.right <= view.left))
            return null
        if (view.isInTouchMode && !view.isFocusableInTouchMode)
            return null

        var ancestor = view.parent
        while (ancestor is ViewGroup)
        {
            val vgAncestor = ancestor
            if (vgAncestor.descendantFocusability == ViewGroup.FOCUS_BLOCK_DESCENDANTS)
                return null

            ancestor = vgAncestor.parent
        }

        return null
    }
}
