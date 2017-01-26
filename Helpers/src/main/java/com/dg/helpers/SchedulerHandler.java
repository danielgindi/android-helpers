package com.dg.helpers;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;

public class SchedulerHandler extends Handler
{
    private HashMap<Runnable, Runnable> mTasks = new HashMap<>();

    public SchedulerHandler()
    {
        super();
    }

    public SchedulerHandler(Callback callback)
    {
        super(callback);
    }

    public SchedulerHandler(Looper looper)
    {
        super(looper);
    }

    public SchedulerHandler(Looper looper, Callback callback)
    {
        super(looper, callback);
    }

    public void postWithSchedule(final Runnable task, long delay, final long period)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                task.run();
                postDelayed(this, period);
            }
        };

        mTasks.put(task, runnable);

        postDelayed(runnable, delay);
    }

    public void postWithSchedule(final Runnable task, final long period)
    {
        postWithSchedule(task, period, period);
    }

    public void removeScheduledTask(Runnable task)
    {
        Runnable removed = mTasks.remove(task);
        if (removed == null) return;

        removeCallbacks(removed);
    }
}
