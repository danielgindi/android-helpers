package com.dg.runnable

abstract class RunnableWithArgument<T> : Runnable
{
    var runnableArgument: T? = null

    override fun run()
    {
        runWithArgument(runnableArgument)
    }

    abstract fun runWithArgument(runnableArgument: T?)
}
