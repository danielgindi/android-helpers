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

    /**
     * Loads the font using the default <code>sharedInstance()</code>.
     * This caches the fonts for reuse.
     * @param fontFamily
     * @return The loaded Typeface or null if failed.
     */
    public Typeface getFont(String fontFamily)
    {
        return getFont(fontFamily, true, true);
    }

    /**
     * Loads the font using the default <code>sharedInstance()</code>.
     * @param fontFamily
     * @param loadFromCache Should the font be loaded from cache?
     * @param saveToCache Should the font be saved to cache?
     * @return The loaded Typeface or null if failed.
     */
    public Typeface getFont(String fontFamily, boolean loadFromCache, boolean saveToCache)
    {
        Typeface typeface = loadFromCache ? _fontCache.get(fontFamily) : null;
        if (typeface == null)
        {
            typeface = loadFontFromAssets(_context, fontFamily);
            if (saveToCache && typeface != null)
            {
                _fontCache.put(fontFamily, typeface);
            }
        }
        return typeface;
    }

    /**
     * Loads the font using the default <code>sharedInstance()</code>.
     * This caches the fonts for reuse.
     * @param context
     * @param fontFamily
     * @return The loaded Typeface or null if failed.
     */
    public static Typeface getFont(Context context, String fontFamily)
    {
        return sharedInstance(context).getFont(fontFamily);
    }

    /**
     * Loads the font using the default <code>sharedInstance()</code>.
     * @param context
     * @param fontFamily
     * @param loadFromCache Should the font be loaded from cache?
     * @param saveToCache Should the font be saved to cache?
     * @return The loaded Typeface or null if failed.
     */
    public static Typeface getFont(Context context,
                                   String fontFamily,
                                   boolean loadFromCache,
                                   boolean saveToCache)
    {
        return sharedInstance(context).getFont(fontFamily, loadFromCache, saveToCache);
    }

    /**
     * Loads the font directly from the Assets. No caching done here.
     * @param context
     * @param fontFamily
     * @return The loaded Typeface or null if failed.
     */
    private static Typeface loadFontFromAssets(Context context, String fontFamily)
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
