package com.dg.helpers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class StringHelper
{
    public static String withObject(Object value)
    {
        if (value == null) return null;
        else if (value instanceof String) return (String)value;
        else if (value instanceof Double)
        {
            if ((Double)value == (long)value)
            {
                return ((Long)(long)value).toString();
            }
            else
            {
                return getCleanDecimalFormatter().format((double)value);
            }
        }
        else if (value instanceof Float)
        {
            if ((Float)value == (long)value)
            {
                return ((Long)(long)value).toString();
            }
            else
            {
                return getCleanDecimalFormatter().format((float)value);
            }
        }
        else return value.toString();
    }

    public static String withObject(Object value, String defaultValue)
    {
        if (value == null) return defaultValue;
        else if (value instanceof String) return (String)value;
        else if (value instanceof Double)
        {
            if ((Double)value == (long)value)
            {
                return ((Long)(long)value).toString();
            }
            else
            {
                return getCleanDecimalFormatter().format((double)value);
            }
        }
        else if (value instanceof Float)
        {
            if ((Float)value == (long)value)
            {
                return ((Long)(long)value).toString();
            }
            else
            {
                return getCleanDecimalFormatter().format((float)value);
            }
        }
        else return value.toString();
    }

    private static DecimalFormat sCleanDecimalFormatter;

    private static DecimalFormat getCleanDecimalFormatter()
    {
        if (sCleanDecimalFormatter == null)
        {
            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.getDefault()));
            df.setMaximumFractionDigits(340);
            sCleanDecimalFormatter = df;
        }
        return sCleanDecimalFormatter;
    }

    private static Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\._%+\\-]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+)*$");

    public static boolean isValidEmailAddress(String email)
    {
        return pattern.matcher(email).matches();
    }
}
