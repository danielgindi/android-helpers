package com.dg.helpers

@Suppress("unused")
object CollectionHelper
{
    fun isEmpty(array: Collection<*>?): Boolean
    {
        return array == null || array.isEmpty()
    }

    fun isEmpty(array: Array<Any>?): Boolean
    {
        return array == null || array.isEmpty()
    }
}
