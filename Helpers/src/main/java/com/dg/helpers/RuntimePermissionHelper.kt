package com.dg.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

@Suppress("unused")
object RuntimePermissionHelper
{
    fun isPermissionAvailable(context: Context, permission: String): Boolean
    {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun isAnyPermissionAvailable(context: Context, permissions: Array<String>): Boolean
    {
        for (permission in permissions)
        {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) return true
        }
        return false
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
        return !isPermissionAvailable(activity, permission) &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}
