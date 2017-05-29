package com.dg.helpers;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class DateHelper
{
    private static ThreadLocal<SimpleDateFormat> _utcIsoDateFormatter = new ThreadLocal<>();

    public static SimpleDateFormat utcIsoDateFormatter()
    {
        if (_utcIsoDateFormatter.get() == null)
        {
            TimeZone utcTimezone = TimeZone.getTimeZone("GMT");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            format.setTimeZone(utcTimezone);
            _utcIsoDateFormatter.set(format);
        }
        return _utcIsoDateFormatter.get();
    }

    public static Date withObject(Object value)
    {
        if (value == null) return null;
        if (value instanceof Date) return (Date)value;
        else
        {
            try
            {
                return utcIsoDateFormatter().parse(value.toString());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static boolean isDateComponentEqualToDate(Date date1, Date date2)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR),
                month1 = calendar.get(Calendar.MONTH),
                day1 = calendar.get(Calendar.DATE);
        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR),
                month2 = calendar.get(Calendar.MONTH),
                day2 = calendar.get(Calendar.DATE);

        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    public static boolean isToday(Date date)
    {
        return DateUtils.isToday(date.getTime());
    }
}
