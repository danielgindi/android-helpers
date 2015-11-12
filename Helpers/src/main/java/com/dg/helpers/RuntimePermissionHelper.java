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
    public interface PermissionRequestResultListener { void onRequestPermissionResult(@NonNull String permissions[], @NonNull int[] grantResults); }

    private static AtomicInteger mNextPermissionRequestCode = new AtomicInteger(0x0);
    private static ConcurrentHashMap<Integer, PermissionRequestResultListener> mRequestPermissionResultListeners = new ConcurrentHashMap<Integer, PermissionRequestResultListener>();
    private static List mDeniedPermissionList = Collections.synchronizedList(new ArrayList());

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

    public static void requestPermission(Activity activity, String permission, PermissionRequestResultListener listener)
    {
        requestPermissions(activity, new String[]{permission}, listener);
    }

    public static void requestPermissions(Activity activity, String[] permissions, PermissionRequestResultListener listener)
    {
        // Can only use lower 8 bits for requestCode
        int code = mNextPermissionRequestCode.getAndIncrement() % 256;
        mRequestPermissionResultListeners.put(code, listener);
        ActivityCompat.requestPermissions(activity, permissions, code);
    }

    public static void handleOnRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        for (int i = 0; i < permissions.length; i++)
        {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
            {
                if (mDeniedPermissionList.contains(permissions[i]))
                {
                    mDeniedPermissionList.remove(permissions[i]);
                }
            }
            else
            {
                if (!mDeniedPermissionList.contains(permissions[i]))
                {
                    mDeniedPermissionList.add(permissions[i]);
                }
            }
        }

        PermissionRequestResultListener listener = mRequestPermissionResultListeners.remove(requestCode);
        if (listener != null)
        {
            mRequestPermissionResultListeners.remove(listener);
            listener.onRequestPermissionResult(permissions, grantResults);
        }
    }

    public static boolean isPermissionDenied(String permission)
    {
        return mDeniedPermissionList.contains(permission);
    }

    public static boolean areAnyPermissionsDenied(String[] permissions)
    {
        for (String permission : permissions)
        {
            if (mDeniedPermissionList.contains(permission)) return true;
        }
        return false;
    }
}
