/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dg.helpers

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.media.ExifInterface
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface

import java.io.IOException
import java.io.OutputStream
import java.util.Locale

object BitmapHelper
{

    fun getScaledBitmapFromBitmap(bmp: Bitmap,
                                  maxWidth: Int,
                                  maxHeight: Int,
                                  keepAspectRatio: Boolean,
                                  freeOldBitmap: Boolean): Bitmap
    {
        var maxWidth = maxWidth
        var maxHeight = maxHeight
        if (keepAspectRatio)
        {
            val ratio = if (bmp.width == 0) 1f else bmp.width / bmp.height.toFloat()
            val newRatio = if (maxHeight == 0) 1f else maxWidth / maxHeight.toFloat()

            if (newRatio > ratio)
            {
                maxWidth = (maxHeight.toFloat() * ratio).toInt()
            }
            else if (newRatio < ratio)
            {
                maxHeight = (maxWidth.toFloat() / ratio).toInt()
            }
        }

        val scaled = Bitmap.createScaledBitmap(bmp, maxWidth, maxHeight, true)

        if (freeOldBitmap && scaled != bmp)
        {
            bmp.recycle()
        }

        return scaled
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, minWidth: Int, minHeight: Int): Int
    {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth

        var inSampleSize = 1

        if (minHeight in 1..(height - 1) || minWidth in 1..(width - 1))
        {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((minHeight <= 0 || halfHeight / inSampleSize > minHeight) && (minWidth <= 0 || halfWidth / inSampleSize > minWidth))
            {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun writeScaledImageFromFileToStream(filePath: String,
                                         sizeConstraint: Point,
                                         compressFormat: Bitmap.CompressFormat,
                                         compressQuality: Int,
                                         outputStream: OutputStream): Boolean
    {
        var image: Bitmap? = null
        var success = false
        try
        {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            options.inSampleSize = BitmapHelper.calculateInSampleSize(options, sizeConstraint.x, sizeConstraint.y)
            options.inJustDecodeBounds = false
            image = BitmapFactory.decodeFile(filePath, options)

            // Try to fix rotation, as the file looses the EXIF data when re-compressed
            try
            {
                image = getBitmapByFixingRotationForFile(filePath, image, null, true)
            }
            catch (ignored: OutOfMemoryError)
            {
            }
            catch (ignored: Exception)
            {
            }

            // Compress directly to the output stream
            image!!.compress(compressFormat, compressQuality, outputStream)

            // We're Ok
            success = true
        }
        catch (ignored: OutOfMemoryError)
        {
        }
        catch (ignored: Exception)
        {
        }

        image?.recycle()

        return success
    }

    fun getImageSize(filePath: String): Point
    {
        try
        {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            return Point(options.outWidth, options.outHeight)
        }
        catch (ignored: OutOfMemoryError)
        {
        }
        catch (ignored: Exception)
        {
        }

        return Point(0, 0)
    }

    fun fixBitmapRotationExif(filePath: String, activityForScreenOrientation: Activity?)
    {
        try
        {
            val exif = ExifInterface(filePath)

            val exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            if (exifOrientation == ExifInterface.ORIENTATION_UNDEFINED && Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("htc"))
                return

            var flippedHorizontally = false
            var flippedVertically = false

            var angle = 0

            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                angle += 90
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                angle += 180
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                angle += 270
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL)
            {
                flippedHorizontally = true
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_FLIP_VERTICAL)
            {
                flippedVertically = true
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_TRANSPOSE)
            {
                angle += 90
                flippedVertically = true
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_TRANSVERSE)
            {
                angle -= 90
                flippedVertically = true
            }

            var orientation: Int

            if (activityForScreenOrientation != null)
            {
                orientation = getScreenOrientation(activityForScreenOrientation)
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                {
                    angle += 90
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
                {
                    angle += 180
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
                {
                    angle += 270
                }
            }

            orientation = 0
            angle %= 360

            if (angle == -90 && flippedVertically && !flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSVERSE
            }
            else if (angle == -270 && flippedVertically && !flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSPOSE
            }
            else if (angle == -90 && !flippedVertically && flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSPOSE
            }
            else if (angle == -270 && !flippedVertically && flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSVERSE
            }
            else
            {
                while (angle < 0)
                {
                    angle += 360
                }
                when (angle)
                {
                    0 -> if (flippedHorizontally)
                    {
                        orientation = ExifInterface.ORIENTATION_FLIP_HORIZONTAL
                    }
                    else if (flippedVertically)
                    {
                        orientation = ExifInterface.ORIENTATION_FLIP_VERTICAL
                    }
                    90 -> orientation = ExifInterface.ORIENTATION_ROTATE_90
                    180 -> orientation = ExifInterface.ORIENTATION_ROTATE_180
                    270 -> orientation = ExifInterface.ORIENTATION_ROTATE_270
                }
            }

            if (orientation != exifOrientation)
            {
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, orientation.toString())
                exif.saveAttributes()
            }
        }
        catch (e: IOException)
        {
            Log.w("TAG", "-- Error in setting image")
        }

    }

    fun getBitmapByFixingRotationForFile(filePath: String,
                                         sourceBitmap: Bitmap?,
                                         activityForScreenOrientation: Activity?,
                                         freeSourceBitmap: Boolean): Bitmap?
    {
        try
        {
            val exif = ExifInterface(filePath)
            var orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            if (orientation == ExifInterface.ORIENTATION_UNDEFINED && Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("htc"))
                return null

            var flippedHorizontally = false
            var flippedVertically = false

            var angle = 0

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                angle += 90
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                angle += 180
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                angle += 270
            }
            else if (orientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL)
            {
                flippedHorizontally = true
            }
            else if (orientation == ExifInterface.ORIENTATION_FLIP_VERTICAL)
            {
                flippedVertically = true
            }
            else if (orientation == ExifInterface.ORIENTATION_TRANSPOSE)
            {
                angle += 90
                flippedVertically = true
            }
            else if (orientation == ExifInterface.ORIENTATION_TRANSVERSE)
            {
                angle -= 90
                flippedVertically = true
            }

            if (activityForScreenOrientation != null)
            {
                orientation = getScreenOrientation(activityForScreenOrientation)
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                {
                    angle += 90
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
                {
                    angle += 180
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
                {
                    angle += 270
                }
            }

            var bmp = sourceBitmap
            if (bmp == null)
            {
                bmp = BitmapFactory.decodeFile(filePath, null)
            }
            if (angle != 0)
            {
                val mat = Matrix()
                mat.postRotate(angle.toFloat())

                if (flippedHorizontally)
                {
                    mat.postScale(-1f, 1f)
                }
                if (flippedVertically)
                {
                    mat.postScale(1f, -1f)
                }

                val rotated = Bitmap.createBitmap(bmp!!, 0, 0, bmp.width, bmp.height, mat, true)
                if (freeSourceBitmap || bmp != sourceBitmap)
                {
                    bmp.recycle()
                }
                bmp = rotated
            }

            return bmp

        }
        catch (e: IOException)
        {
            Log.w("TAG", "-- Error in setting image")
        }
        catch (oom: OutOfMemoryError)
        {
            Log.w("TAG", "-- OOM Error in setting image")
        }

        return null
    }

    fun getScreenOrientation(activity: Activity): Int
    {
        val rotation = activity.windowManager.defaultDisplay.rotation
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        val orientation: Int
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && width > height)
        {
            orientation = when (rotation)
            {
                Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                else ->
                {
                    Log.e("getScreenOrientation", "Unknown screen orientation. Defaulting to portrait.")
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }
        else
        {
            // if the device's natural orientation is landscape or if the device
            // is square:

            orientation = when (rotation)
            {
                Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                else ->
                {
                    Log.e("getScreenOrientation", "Unknown screen orientation. Defaulting to landscape.")
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            }
        }

        return orientation
    }
}