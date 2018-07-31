package com.dg.helpers

object FloatHelper
{
    fun withObject(value: Any?): Float?
    {
        when (value)
        {
            null -> return null
            is Double -> return value.toFloat()
            is Float -> return value
            is Int -> return value.toFloat()
            is Short -> return value.toFloat()
            is String -> try
            {
                return java.lang.Float.parseFloat(value)
            }
            catch (ignored: Exception)
            {
            }
        }
        return null
    }

    fun withObject(value: Any?, defaultValue: Float): Float
    {
        return withObject(value) ?: defaultValue
    }

    fun toArray(inArray: Array<Float?>): FloatArray
    {
        val array = FloatArray(inArray.size)
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
