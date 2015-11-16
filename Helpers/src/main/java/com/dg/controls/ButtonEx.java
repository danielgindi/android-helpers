package com.dg.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dg.R;
import com.dg.helpers.FontHelper;

import java.util.Locale;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */

public class ButtonEx extends android.widget.Button
{
    public ButtonEx(Context context)
    {
        super(context);
    }

    public ButtonEx(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setCustomFontFamily(context, attrs);
    }

    public ButtonEx(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setCustomFontFamily(context, attrs);
    }

    static String _namespace;
    private void setCustomFontFamily(Context context, AttributeSet attrs)
    {
        String fontFamily = null;
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomFontFamily);
        try
        {
            fontFamily = styledAttributes.getString(R.styleable.CustomFontFamily_customFontFamily);
        }
        finally
        {
            styledAttributes.recycle();
        }

        if (fontFamily != null && !fontFamily.isEmpty())
        {
            setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.LINEAR_TEXT_FLAG);
            setCustomFont(context, fontFamily);
        }
    }

    public boolean setCustomFont(Context context, String fontFamily)
    {
        Typeface typeface = FontHelper.sharedInstance(context).getFont(fontFamily);
        if (typeface != null)
        {
            setTypeface(typeface);
            return true;
        }
        else
        {
            return false;
        }
    }
}
