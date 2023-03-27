package com.dg.helpers

import java.math.BigDecimal

@Suppress("unused")
object BigDecimalHelper
{
    @Suppress("MemberVisibilityCanBePrivate")
    fun withObject(value: Any?): BigDecimal?
    {
        return if (value == null)
            null
        else value as? BigDecimal ?: when (value)
        {
            is Double -> if (value.isNaN()) null else BigDecimal(value)
            is Float -> if (value.isNaN()) null else BigDecimal(value.toDouble())
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
