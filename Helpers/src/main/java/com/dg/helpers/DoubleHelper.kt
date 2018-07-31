package com.dg.helpers

object DoubleHelper
{
    fun withObject(value: Any?): Double?
    {
        when (value)
        {
            null -> return null
            is Double -> return value
            is Float -> return value.toDouble()
            is Int -> return value.toDouble()
            is Short -> return value.toDouble()
            is String -> try
            {
                return java.lang.Double.parseDouble(value as String?)
            }
            catch (ignored: Exception)
            {
            }
        }
        return null
    }

    fun withObject(value: Any?, defaultValue: Double): Double
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
