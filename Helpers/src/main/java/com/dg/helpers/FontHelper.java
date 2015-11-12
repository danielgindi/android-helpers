package com.dg.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 *
 * This loads the specified font from the assets folder.
 * If the path specified does not contain path and extension
 *   - it will search for assets/fonts/[fontname].[ttf|otf]
 */
public class FontHelper
{
    private static FontHelper _instance;
    private Context _context;
    private HashMap<String, Typeface> _fontCache = new HashMap<String, Typeface>();

    public static FontHelper sharedInstance(Context context)
    {
        if (_instance == null)
        {
            _instance = new FontHelper(context.getApplicationContext());
        }
        return _instance;
    }

    private FontHelper(Context context)
    {
        _context = context;
    }

    public Typeface getFont(String fontFamily)
    {
        Typeface typeface = _fontCache.get(fontFamily);
        if (typeface == null)
        {
            typeface = loadFont(_context, fontFamily);
            if (typeface != null)
            {
                _fontCache.put(fontFamily, typeface);
            }
        }
        return typeface;
    }

    public static Typeface loadFont(Context context, String fontFamily)
    {
        if (!fontFamily.contains(".") && !fontFamily.contains("/"))
        {
            String ttfFontFamily = "fonts/" + fontFamily + ".ttf";

            try
            {
                return Typeface.createFromAsset(context.getResources().getAssets(), ttfFontFamily);
            }
            catch (Exception ignored)
            {
            }

            String otfFontFamily = "fonts/" + fontFamily + ".otf";

            try
            {
                return Typeface.createFromAsset(context.getResources().getAssets(), otfFontFamily);
            }
            catch (Exception ignored)
            {
            }
        }

        try
        {
            return Typeface.createFromAsset(context.getResources().getAssets(), fontFamily);
        }
        catch (Exception e)
        {
            Log.e("FontHelper", e.getMessage());
            return null;
        }
    }
}
