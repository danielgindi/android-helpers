package com.dg.helpers

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.regex.Pattern

@Suppress("unused")
object StringHelper
{

    private var sCleanDecimalFormatter: DecimalFormat? = null

    private val cleanDecimalFormatter: DecimalFormat
        get()
        {
            if (sCleanDecimalFormatter == null)
            {
                val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.getDefault()))
                df.maximumFractionDigits = 340
                sCleanDecimalFormatter = df
            }
            return sCleanDecimalFormatter as DecimalFormat
        }

    private val pattern = Pattern.compile("^[a-zA-Z0-9\\._%+\\-]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+)*$")
    fun withObject(value: Any?): String?
    {
        when (value)
        {
            null -> return null
            is String -> return value
            is Double ->
            {
                return if (value.toLong().toDouble() == value)
                {
                    value.toLong().toString()
                }
                else
                {
                    cleanDecimalFormatter.format(value)
                }
            }
            is Float ->
            {
                return if (value.toLong().toFloat() == value)
                {
                    value.toLong().toString()
                }
                else
                {
                    cleanDecimalFormatter.format(value.toDouble())
                }
            }
            else -> return value.toString()
        }
    }

    fun withObject(value: Any?, defaultValue: String): String
    {
        return withObject(value) ?: defaultValue
    }

    fun isValidEmailAddress(email: String): Boolean
    {
        return pattern.matcher(email).matches()
    }
}
