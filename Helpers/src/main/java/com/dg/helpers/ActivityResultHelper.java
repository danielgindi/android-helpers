package com.dg.helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 *
 * @class ActivityResultHelper
 * Helpers for handling starting activities.
 *
 * Preparations: 
 *   Override onActivityResult(...) in your Activities,
 *   and forward the calls to ActivityResultHelper.handleOnActivityResult(...)
 *
 *   Example:
 *     @Override
 *     public void onActivityResult(int requestCode, int resultCode, Intent intent)
 *     {
 *         ActivityResultHelper.handleOnActivityResult(requestCode, resultCode, intent);
 *     }
 */
public class ActivityResultHelper
{
    public interface ActivityResultListener { void onActivityResult(int resultCode, Intent intent); }

    private static AtomicInteger mNextActivityRequestCode = new AtomicInteger(1);
    public static ConcurrentHashMap<Integer, ActivityResultListener> mActivityResultListeners = new ConcurrentHashMap<Integer, ActivityResultListener>();

    public static void listenForActivityResult(int requestCode, ActivityResultListener resultListener)
    {
        mActivityResultListeners.put(requestCode, resultListener);
    }

    public static void stopListeningForActivityResult(int requestCode, ActivityResultListener resultListener)
    {
        mActivityResultListeners.remove(requestCode);
    }

    public static int generateRequestCodeForActivity()
    {
        return mNextActivityRequestCode.getAndIncrement();
    }

    public static void handleOnActivityResult(int requestCode, int resultCode, Intent intent)
    {
        ActivityResultListener listener = mActivityResultListeners.remove(requestCode);
        if (listener != null)
        {
            mActivityResultListeners.remove(listener);
            listener.onActivityResult(resultCode, intent);
        }
    }

    public static void startActivityForResult(@NonNull Activity parentActivity,
                                       Intent intent, ActivityResultListener resultListener)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentActivity.startActivityForResult(intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void startActivityForResult(@NonNull Activity parentActivity,
                                       Intent intent, ActivityResultListener resultListener,
                                       @Nullable Bundle options)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentActivity.startActivityForResult(intent, requestCode, options);
    }

    public static void startActivityForResult(@NonNull Fragment parentFragment,
                                              Intent intent, ActivityResultListener resultListener)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentFragment.startActivityForResult(intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void startActivityForResult(@NonNull Fragment parentFragment,
                                              Intent intent, ActivityResultListener resultListener,
                                              @Nullable Bundle options)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentFragment.startActivityForResult(intent, requestCode, options);
    }

    public static void startActivityFromChild(@NonNull Activity parentActivity,
                                              Activity childActivity,
                                              Intent intent, ActivityResultListener resultListener)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentActivity.startActivityFromChild(childActivity, intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void startActivityFromChild(@NonNull Activity parentActivity,
                                              Activity childActivity,
                                              Intent intent, ActivityResultListener resultListener,
                                              @Nullable Bundle options)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentActivity.startActivityFromChild(childActivity, intent, requestCode, options);
    }

    public static void startActivityIfNeeded(@NonNull Activity parentActivity,
                                      Intent intent, ActivityResultListener resultListener)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentActivity.startActivityIfNeeded(intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void startActivityIfNeeded(@NonNull Activity parentActivity,
                                      Intent intent, ActivityResultListener resultListener,
                                      @Nullable Bundle options)
    {
        int requestCode = generateRequestCodeForActivity();
        listenForActivityResult(requestCode, resultListener);
        parentActivity.startActivityIfNeeded(intent, requestCode, options);
    }
}

