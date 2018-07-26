package com.dg.helpers

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
