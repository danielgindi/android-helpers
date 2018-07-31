package com.dg.helpers

import java.math.BigDecimal

object BigDecimalHelper
{
    fun withObject(value: Any?): BigDecimal?
    {
        return if (value == null)
            null
        else value as? BigDecimal ?: when (value)
        {
            is Double -> BigDecimal(value)
            is Float -> BigDecimal(value.toDouble())
            is Int -> BigDecimal(value)
            is Short -> BigDecimal(value.toInt())
            is String -> BigDecimal(value)
            else -> null
        }
    }

    fun withObject(value: Any?, defaultValue: BigDecimal): BigDecimal
    {
        return withObject(value) ?: defaultValue
    }
}
