package com.dg.helpers

import android.text.format.DateUtils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Suppress("unused")
object DateHelper
{
    private val mIso8601 = ThreadLocal<SimpleDateFormat>()
    private val mIso8601_sss = ThreadLocal<SimpleDateFormat>()

    val iso8601: SimpleDateFormat
        get()
        {
            if (mIso8601.get() == null)
            {
                val utcTimezone = TimeZone.getTimeZone("GMT")
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                format.timeZone = utcTimezone
                mIso8601.set(format)
            }
            return mIso8601.get()
        }

    val iso8601_sss: SimpleDateFormat
        get()
        {
            if (mIso8601_sss.get() == null)
            {
                val utcTimezone = TimeZone.getTimeZone("GMT")
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                format.timeZone = utcTimezone
                mIso8601_sss.set(format)
            }
            return mIso8601_sss.get()
        }

    // Backwards compatibility
    fun utcIsoDateFormatter(): SimpleDateFormat
    {
        return iso8601
    }

    fun withObject(value: Any?): Date?
    {
        if (value == null) return null
        if (value is Date) return value

        try
        {
            return iso8601_sss.parse(value.toString())
        }
        catch (e: ParseException)
        {
            try
            {
                return iso8601.parse(value.toString())
            }
            catch (e: ParseException)
            {
                e.printStackTrace()
            }
        }

        return null
    }

    fun isDateComponentEqualToDate(date1: Date, date2: Date): Boolean
    {
        val calendar = Calendar.getInstance()

        calendar.time = date1
        val year1 = calendar.get(Calendar.YEAR)
        val month1 = calendar.get(Calendar.MONTH)
        val day1 = calendar.get(Calendar.DATE)
        calendar.time = date2
        val year2 = calendar.get(Calendar.YEAR)
        val month2 = calendar.get(Calendar.MONTH)
        val day2 = calendar.get(Calendar.DATE)

        return year1 == year2 && month1 == month2 && day1 == day2
    }

    fun isToday(date: Date): Boolean
    {
        return DateUtils.isToday(date.time)
    }
}
