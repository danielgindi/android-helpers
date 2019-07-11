package com.dg.helpers

@Suppress("unused")
object IntHelper
{
    @Suppress("MemberVisibilityCanBePrivate")
    fun withObject(value: Any?): Int?
    {
        when (value)
        {
            null -> return null
            is Int -> return value
            is Short -> return value.toInt()
            is String -> try
            {
                return Integer.parseInt(value)
            }
            catch (ignored: Exception)
            {
            }
        }
        return null
    }

    fun withObject(value: Any?, defaultValue: Int): Int
    {
        return withObject(value) ?: defaultValue
    }

    fun toArray(inArray: Array<Int?>): IntArray
    {
        val array = IntArray(inArray.size)
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
