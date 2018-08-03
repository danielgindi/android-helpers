package com.dg.helpers

import android.content.Context
import android.graphics.Typeface
import android.util.Log

import java.util.HashMap

/**
 * This loads the specified font from the assets folder.
 * If the path specified does not contain path and extension
 * - it will search for assets/fonts/[fontname].[ttf|otf]
 */
@Suppress("unused")
class FontHelper private constructor(private val _context: Context)
{
    private val _fontCache = HashMap<String, Typeface>()

    /**
     * Loads the font using the default `sharedInstance()`.
     * @param fontFamily
     * @param loadFromCache Should the font be loaded from cache?
     * @param saveToCache Should the font be saved to cache?
     * @return The loaded Typeface or null if failed.
     */
    @JvmOverloads
    fun getFont(fontFamily: String,
                loadFromCache: Boolean = true,
                saveToCache: Boolean = true): Typeface?
    {
        var typeface: Typeface? = if (loadFromCache) _fontCache[fontFamily] else null
        if (typeface == null)
        {
            typeface = loadFontFromAssets(_context, fontFamily)
            if (saveToCache && typeface != null)
            {
                _fontCache[fontFamily] = typeface
            }
        }
        return typeface
    }

    companion object
    {
        private var _instance: FontHelper? = null

        fun sharedInstance(context: Context): FontHelper
        {
            if (_instance == null)
            {
                _instance = FontHelper(context.applicationContext)
            }
            return _instance!!
        }

        /**
         * Loads the font using the default `sharedInstance()`.
         * This caches the fonts for reuse.
         * @param context
         * @param fontFamily
         * @return The loaded Typeface or null if failed.
         */
        fun getFont(context: Context, fontFamily: String): Typeface?
        {
            return sharedInstance(context).getFont(fontFamily)
        }

        /**
         * Loads the font using the default `sharedInstance()`.
         * @param context
         * @param fontFamily
         * @param loadFromCache Should the font be loaded from cache?
         * @param saveToCache Should the font be saved to cache?
         * @return The loaded Typeface or null if failed.
         */
        fun getFont(context: Context,
                    fontFamily: String,
                    loadFromCache: Boolean,
                    saveToCache: Boolean): Typeface?
        {
            return sharedInstance(context).getFont(fontFamily, loadFromCache, saveToCache)
        }

        /**
         * Loads the font directly from the Assets. No caching done here.
         * @param context
         * @param fontFamily
         * @return The loaded Typeface or null if failed.
         */
        private fun loadFontFromAssets(context: Context, fontFamily: String): Typeface?
        {
            if (!fontFamily.contains(".") && !fontFamily.contains("/"))
            {
                val ttfFontFamily = "fonts/$fontFamily.ttf"

                try
                {
                    return Typeface.createFromAsset(context.resources.assets, ttfFontFamily)
                }
                catch (ignored: Exception)
                {
                }

                val otfFontFamily = "fonts/$fontFamily.otf"

                try
                {
                    return Typeface.createFromAsset(context.resources.assets, otfFontFamily)
                }
                catch (ignored: Exception)
                {
                }

            }

            try
            {
                return Typeface.createFromAsset(context.resources.assets, fontFamily)
            }
            catch (e: Exception)
            {
                Log.e("FontHelper", e.message)
                return null
            }

        }
    }
}
/**
 * Loads the font using the default `sharedInstance()`.
 * This caches the fonts for reuse.
 * @param fontFamily
 * @return The loaded Typeface or null if failed.
 */
