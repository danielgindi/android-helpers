package com.dg.helpers

@Suppress("unused")
object LongHelper
{
    @Suppress("MemberVisibilityCanBePrivate")
    fun withObject(value: Any?): Long?
    {
        when (value)
        {
            null -> return null
            is Long -> return value
            is Int -> return value.toLong()
            is Short -> return value.toLong()
            is String -> try
            {
                return java.lang.Long.parseLong(value)
            }
            catch (ignored: Exception)
            {
            }
        }
        return null
    }

    fun withObject(value: Any?, defaultValue: Long): Long
    {
        return withObject(value) ?: defaultValue
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
