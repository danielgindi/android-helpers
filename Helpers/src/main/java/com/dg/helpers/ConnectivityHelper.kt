package com.dg.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

/**
 * Check device's network connectivity and speed
 * @author emil http://stackoverflow.com/users/220710/emil
 */
object ConnectivityHelper
{

    /**
     * Get the network info
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getNetworkInfo(context: Context): NetworkInfo?
    {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean
    {
        val info = ConnectivityHelper.getNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    fun isConnectedWifi(context: Context): Boolean
    {
        val info = ConnectivityHelper.getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    fun isConnectedMobile(context: Context): Boolean
    {
        val info = ConnectivityHelper.getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    fun isConnectedFast(context: Context): Boolean
    {
        val info = ConnectivityHelper.getNetworkInfo(context)
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return info != null && info.isConnected && ConnectivityHelper.isConnectionFast(info.type, tm.networkType)
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    fun isConnectionFast(type: Int, subType: Int): Boolean
    {
        return if (type == ConnectivityManager.TYPE_WIFI)
        {
            true
        }
        else if (type == ConnectivityManager.TYPE_MOBILE)
        {
            when (subType)
            {
                TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps

                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                // API level 11
                TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps
                // API level 9
                TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps
                // API level 13
                TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps
                // API level 8
                TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                // API level 11
                TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                // Unknown
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                else -> false
            }
        }
        else
        {
            false
        }
    }
}
