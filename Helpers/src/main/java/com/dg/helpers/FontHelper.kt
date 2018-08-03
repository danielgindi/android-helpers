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
class FontHelper private constructor()
{
    companion object
    {
        private val sFontCache = HashMap<String, Typeface>()

        fun clearCache()
        {
            sFontCache.clear()
        }

        fun cacheSize(): Int
        {
            return sFontCache.size
        }
        
        /**
         * Loads the font with possible use of the static cache
         * @param context
         * @param fontFamily
         * @param loadFromCache Should the font be loaded from cache?
         * @param saveToCache Should the font be saved to cache?
         * @return The loaded Typeface or null if failed.
         */
        fun getFont(context: Context,
                    fontFamily: String,
                    loadFromCache: Boolean = true,
                    saveToCache: Boolean = true): Typeface?
        {
            var typeface: Typeface? = if (loadFromCache) sFontCache[fontFamily] else null
            if (typeface == null)
            {
                typeface = loadFontFromAssets(context, fontFamily)
                if (saveToCache && typeface != null)
                {
                    sFontCache[fontFamily] = typeface
                }
            }
            return typeface
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