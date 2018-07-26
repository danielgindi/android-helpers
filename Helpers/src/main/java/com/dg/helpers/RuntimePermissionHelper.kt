package com.dg.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import java.util.ArrayList
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * @class RuntimePermissionHelper
 * Helpers for handling runtime permissions.
 *
 * Preparations:
 * Override onRequestPermissionResult(...) in your Activities,
 * and forward the calls to RuntimePermissionHelper.handleOnRequestPermissionsResult(...)
 *
 * Example:
 * @Override
 * public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
 * {
 * RuntimePermissionHelper.handleOnRequestPermissionsResult(requestCode, permissions, grantResults);
 * }
 */
object RuntimePermissionHelper
{
    fun isPermissionAvailable(context: Context, permission: String): Boolean
    {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun areAllPermissionAvailable(context: Context, permissions: Array<String>): Boolean
    {
        for (permission in permissions)
        {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    fun isPermissionExplanationRequired(activity: Activity, permission: String): Boolean
    {
        return !isPermissionAvailable(activity, permission) && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}
