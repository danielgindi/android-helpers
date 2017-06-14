package com.dg.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 *
 * @class RuntimePermissionHelper
 * Helpers for handling runtime permissions.
 *
 * Preparations: 
 *   Override onRequestPermissionResult(...) in your Activities, 
 *   and forward the calls to RuntimePermissionHelper.handleOnRequestPermissionsResult(...)
 * 
 *   Example:
 *     @Override
 *     public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
 *     {
 *         RuntimePermissionHelper.handleOnRequestPermissionsResult(requestCode, permissions, grantResults);
 *     }
 */
public class RuntimePermissionHelper
{
    public static boolean isPermissionAvailable(Context context, String permission)
    {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean areAllPermissionAvailable(Context context, String[] permissions)
    {
        for (String permission : permissions)
        {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    public static boolean isPermissionExplanationRequired(Activity activity, String permission)
    {
        return !isPermissionAvailable(activity, permission) &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
