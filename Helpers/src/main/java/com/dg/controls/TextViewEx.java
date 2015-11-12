package com.dg.controls;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dg.helpers.FontHelper;

import java.util.Locale;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */

public class TextViewEx extends android.widget.TextView
{
    private static final String ATTRIBUTE_FONT_FAMILY = "customFontFamily";

    public TextViewEx(Context context)
    {
        super(context);
    }

    public TextViewEx(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setCustomFontFamily(context, attrs);
    }

    public TextViewEx(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setCustomFontFamily(context, attrs);
    }

    static String _namespace;
    private void setCustomFontFamily(Context context, AttributeSet attrs)
    {
        Locale currentLocale = getResources().getConfiguration().locale;
        String lang = currentLocale.getLanguage(), langCountry = currentLocale.getCountry();
        String iso2LangUnderscore = langCountry.length() > 0 ? lang + "_" + langCountry : lang;
        if (_namespace == null)
        {
            //_namespace = "http://schemas.android.com/apk/res/" + context.getPackageName();
            _namespace = "http://schemas.android.com/apk/res-auto";
        }

        String fontFamily = attrs.getAttributeValue(_namespace, ATTRIBUTE_FONT_FAMILY + "_" + iso2LangUnderscore);
        if (fontFamily == null) fontFamily = attrs.getAttributeValue(_namespace, ATTRIBUTE_FONT_FAMILY + "_" + lang);
        if (fontFamily == null) fontFamily = attrs.getAttributeValue(_namespace, ATTRIBUTE_FONT_FAMILY);

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
