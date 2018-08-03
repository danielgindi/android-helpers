package com.dg.runnable

@Suppress("unused")
abstract class RunnableWithArgument<T> : Runnable
{
    var argument: T? = null

    override fun run()
    {
        runWithArgument(argument)
    }

    abstract fun runWithArgument(argument: T?)
}
