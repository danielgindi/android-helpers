package com.dg.helpers;

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
        else return value.toString();
    }

    public static String withObject(Object value, String defaultValue)
    {
        if (value == null) return defaultValue;
        else if (value instanceof String) return (String)value;
        else return value.toString();
    }

    private static Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\._%+\\-]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+)*$");

    public static boolean isValidEmailAddress(String email)
    {
        return pattern.matcher(email).matches();
    }
}
