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

package com.dg.helpers;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class BitmapHelper
{

    public static Bitmap getScaledBitmapFromBitmap(Bitmap bmp, int maxWidth, int maxHeight, boolean keepAspectRatio, boolean freeOldBitmap)
    {
        if (keepAspectRatio)
        {
            float ratio = bmp.getWidth() == 0 ? 1.f : (bmp.getWidth() / (float) bmp.getHeight());
            float newRatio = maxHeight == 0 ? 1.f : (maxWidth / (float) maxHeight);

            if (newRatio > ratio)
            {
                maxWidth = (int) ((float) maxHeight * ratio);
            }
            else if (newRatio < ratio)
            {
                maxHeight = (int) ((float) maxWidth / ratio);
            }
        }

        Bitmap scaled = Bitmap.createScaledBitmap(bmp, maxWidth, maxHeight, true);

        if (freeOldBitmap && scaled != bmp)
        {
            bmp.recycle();
        }

        return scaled;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int minWidth, int minHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if ((minHeight > 0 && height > minHeight) || (minWidth > 0 && width > minWidth))
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((minHeight <= 0 || (halfHeight / inSampleSize) > minHeight) && (minWidth <= 0 || (halfWidth / inSampleSize) > minWidth))
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static boolean writeScaledImageFromFileToStream(String filePath, Point sizeConstraint, Bitmap.CompressFormat compressFormat, int compressQuality, OutputStream outputStream)
    {
        Bitmap image = null;
        boolean success = false;
        try
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = BitmapHelper.calculateInSampleSize(options, sizeConstraint.x, sizeConstraint.y);
            options.inJustDecodeBounds = false;
            image = BitmapFactory.decodeFile(filePath, options);

            // Try to fix rotation, as the file looses the EXIF data when re-compressed
            try
            {
                image = getBitmapByFixingRotationForFile(filePath, image, null, true);
            }
            catch (OutOfMemoryError ignored)
            {
            }
            catch (Exception ignored)
            {
            }

            // Compress directly to the output stream
            image.compress(compressFormat, compressQuality, outputStream);

            // We're Ok
            success = true;
        }
        catch (OutOfMemoryError ignored)
        {
        }
        catch (Exception ignored)
        {
        }

        if (image != null)
        {
            image.recycle();
        }

        return success;
    }

    public static Point getImageSize(String filePath)
    {
        try
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            return new Point(options.outWidth, options.outHeight);
        }
        catch (OutOfMemoryError ignored)
        {
        }
        catch (Exception ignored)
        {
        }

        return new Point(0, 0);
    }

    public static void fixBitmapRotationExif(String filePath, Activity activityForScreenOrientation)
    {
        try
        {
            ExifInterface exif = new ExifInterface(filePath);

            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (exifOrientation == ExifInterface.ORIENTATION_UNDEFINED &&
                    Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("htc")) return;

            boolean flippedHorizontally = false,
                    flippedVertically = false;

            int angle = 0;

            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                angle += 90;
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                angle += 180;
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                angle += 270;
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL)
            {
                flippedHorizontally = true;
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_FLIP_VERTICAL)
            {
                flippedVertically = true;
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_TRANSPOSE)
            {
                angle += 90;
                flippedVertically = true;
            }
            else if (exifOrientation == ExifInterface.ORIENTATION_TRANSVERSE)
            {
                angle -= 90;
                flippedVertically = true;
            }

            int orientation;

            if (activityForScreenOrientation != null)
            {
                orientation = getScreenOrientation(activityForScreenOrientation);
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                {
                    angle += 90;
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
                {
                    angle += 180;
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
                {
                    angle += 270;
                }
            }

            orientation = 0;
            angle = angle % 360;

            if (angle == -90 && flippedVertically && !flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSVERSE;
            }
            else if (angle == -270 && flippedVertically && !flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSPOSE;
            }
            else if (angle == -90 && !flippedVertically && flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSPOSE;
            }
            else if (angle == -270 && !flippedVertically && flippedHorizontally)
            {
                orientation = ExifInterface.ORIENTATION_TRANSVERSE;
            }
            else
            {
                while (angle < 0)
                {
                    angle += 360;
                }
                switch (angle)
                {
                    case 0:
                        if (flippedHorizontally)
                        {
                            orientation = ExifInterface.ORIENTATION_FLIP_HORIZONTAL;
                        }
                        else if (flippedVertically)
                        {
                            orientation = ExifInterface.ORIENTATION_FLIP_VERTICAL;
                        }
                        break;
                    case 90:
                        orientation = ExifInterface.ORIENTATION_ROTATE_90;
                        break;
                    case 180:
                        orientation = ExifInterface.ORIENTATION_ROTATE_180;
                        break;
                    case 270:
                        orientation = ExifInterface.ORIENTATION_ROTATE_270;
                        break;
                }
            }

            if (orientation != exifOrientation)
            {
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, ((Integer) orientation).toString());
                exif.saveAttributes();
            }
        }
        catch (IOException e)
        {
            Log.w("TAG", "-- Error in setting image");
        }
    }

    public static Bitmap getBitmapByFixingRotationForFile(String filePath, Bitmap sourceBitmap, Activity activityForScreenOrientation, boolean freeSourceBitmap)
    {
        try
        {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_UNDEFINED &&
                    Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("htc")) return null;

            boolean flippedHorizontally = false,
                    flippedVertically = false;

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                angle += 90;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                angle += 180;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                angle += 270;
            }
            else if (orientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL)
            {
                flippedHorizontally = true;
            }
            else if (orientation == ExifInterface.ORIENTATION_FLIP_VERTICAL)
            {
                flippedVertically = true;
            }
            else if (orientation == ExifInterface.ORIENTATION_TRANSPOSE)
            {
                angle += 90;
                flippedVertically = true;
            }
            else if (orientation == ExifInterface.ORIENTATION_TRANSVERSE)
            {
                angle -= 90;
                flippedVertically = true;
            }

            if (activityForScreenOrientation != null)
            {
                orientation = getScreenOrientation(activityForScreenOrientation);
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                {
                    angle += 90;
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
                {
                    angle += 180;
                }
                else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
                {
                    angle += 270;
                }
            }

            Bitmap bmp = sourceBitmap;
            if (bmp == null)
            {
                bmp = BitmapFactory.decodeFile(filePath, null);
            }
            if (angle != 0)
            {
                Matrix mat = new Matrix();
                mat.postRotate(angle);

                if (flippedHorizontally)
                {
                    mat.postScale(-1.f, 1.f);
                }
                if (flippedVertically)
                {
                    mat.postScale(1.f, -1.f);
                }

                Bitmap rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                if (freeSourceBitmap || bmp != sourceBitmap)
                {
                    bmp.recycle();
                }
                bmp = rotated;
            }

            return bmp;

        }
        catch (IOException e)
        {
            Log.w("TAG", "-- Error in setting image");
        }
        catch (OutOfMemoryError oom)
        {
            Log.w("TAG", "-- OOM Error in setting image");
        }

        return null;
    }

    public static int getScreenOrientation(Activity activity)
    {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height)
        {
            switch (rotation)
            {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e("getScreenOrientation", "Unknown screen orientation. Defaulting to portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        else
        {
            // if the device's natural orientation is landscape or if the device
            // is square:

            switch (rotation)
            {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e("getScreenOrientation", "Unknown screen orientation. Defaulting to landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }
}