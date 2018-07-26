package com.dg.helpers

import android.os.Handler
import android.os.Looper

import java.util.HashMap

class SchedulerHandler : Handler
{
    private val mTasks = HashMap<Runnable, Runnable>()

    constructor() : super()

    constructor(callback: Handler.Callback) : super(callback)

    constructor(looper: Looper) : super(looper)

    constructor(looper: Looper, callback: Handler.Callback) : super(looper, callback)

    fun postWithSchedule(task: Runnable, delay: Long, period: Long)
    {
        val runnable = object : Runnable
        {
            override fun run()
            {
                task.run()
                postDelayed(this, period)
            }
        }

        mTasks[task] = runnable

        postDelayed(runnable, delay)
    }

    fun postWithSchedule(task: Runnable, period: Long)
    {
        postWithSchedule(task, period, period)
    }

    fun removeScheduledTask(task: Runnable)
    {
        val removed = mTasks.remove(task) ?: return

        removeCallbacks(removed)
    }
}
