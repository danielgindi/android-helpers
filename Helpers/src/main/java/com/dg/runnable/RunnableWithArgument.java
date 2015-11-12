package com.dg.runnable;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public abstract class RunnableWithArgument<T> implements Runnable
{
    private T _runnableArgument = null;

    public void setRunnableArgument(T arg)
    {
        _runnableArgument = arg;
    }
    public T getRunnableArgument()
    {
        return _runnableArgument;
    }

    @Override
    public void run()
    {
        runWithArgument(_runnableArgument);
    }

    public abstract void runWithArgument(T runnableArgument);
}
