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

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 *
 * Utilities for handling Executors
 */
public class ExecutorHelper
{
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    public static final int KEEP_ALIVE = 1;

    @SuppressLint("NewApi")
    public static Executor getThreadPoolExecutor()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            return null;
        }

        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    @SuppressLint("NewApi")
    public static Executor getSerialExecutor()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            return null;
        }

        return AsyncTask.SERIAL_EXECUTOR;
    }

    @SuppressLint("NewApi")
    public static Executor newExecutor(final int queueCapacity,
                                       final String threadNamePrefix,
                                       final int corePoolSize,
                                       final int maximumPoolSize,
                                       final long keepAliveTime,
                                       final TimeUnit unit)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            return null;
        }

        final BlockingQueue<Runnable> poolWorkQueue =
                new LinkedBlockingQueue<Runnable>(queueCapacity);

        final ThreadFactory threadFactory = new ThreadFactory()
        {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(@NonNull Runnable r)
            {
                return new Thread(r, threadNamePrefix + " #" + mCount.getAndIncrement());
            }
        };

        return new ThreadPoolExecutor(corePoolSize,
                                      maximumPoolSize,
                                      keepAliveTime,
                                      unit,
                                      poolWorkQueue,
                                      threadFactory);
    }
}