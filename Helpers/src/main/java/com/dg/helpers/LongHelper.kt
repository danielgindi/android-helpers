package com.dg.helpers

object LongHelper
{
    fun withObject(value: Any?): Long?
    {
        if (value == null)
            return null
        else if (value is Long)
            return value
        else if (value is Int)
            return value as Long
        else if (value is Short)
            return value as Long
        else if (value is String)
        {
            try
            {
                return java.lang.Long.parseLong(value as String?)
            }
            catch (ignored: Exception)
            {
            }

        }
        return null
    }

    fun withObject(value: Any?, defaultValue: Long): Long?
    {
        if (value == null)
            return defaultValue
        else if (value is Long)
            return value
        else if (value is Int)
            return value as Long
        else if (value is Short)
            return value as Long
        else if (value is String)
        {
            try
            {
                return java.lang.Long.parseLong(value as String?)
            }
            catch (ignored: Exception)
            {
            }

        }
        return defaultValue
    }

    fun toArray(inArray: Array<Long?>): LongArray
    {
        val array = LongArray(inArray.size)
        var i = 0
        val len = inArray.size
        while (i < len)
        {
            val value = inArray[i]

            if (value != null)
            {
                array[i] = value
            }
            i++
        }
        return array
    }
}
