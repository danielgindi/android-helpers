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

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Utilities for handling Executors
 */
@Suppress("unused")
object ExecutorHelper
{
    val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    val CORE_POOL_SIZE = CPU_COUNT + 1
    val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
    val KEEP_ALIVE = 1

    val threadPoolExecutor: Executor?
        @SuppressLint("NewApi")
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            null
        }
        else AsyncTask.THREAD_POOL_EXECUTOR

    val serialExecutor: Executor?
        @SuppressLint("NewApi")
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            null
        }
        else AsyncTask.SERIAL_EXECUTOR

    @SuppressLint("NewApi")
    fun newExecutor(queueCapacity: Int,
                    threadNamePrefix: String,
                    corePoolSize: Int,
                    maximumPoolSize: Int,
                    keepAliveTime: Long,
                    unit: TimeUnit): Executor?
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            return null
        }

        val poolWorkQueue = LinkedBlockingQueue<Runnable>(queueCapacity)

        val threadFactory = object : ThreadFactory
        {
            private val mCount = AtomicInteger(1)

            override fun newThread(r: Runnable): Thread
            {
                return Thread(r, threadNamePrefix + " #" + mCount.getAndIncrement())
            }
        }

        return ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                poolWorkQueue,
                threadFactory)
    }
}