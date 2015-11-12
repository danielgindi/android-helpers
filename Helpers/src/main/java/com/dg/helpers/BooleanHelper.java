package com.dg.helpers;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class BooleanHelper
{
    public static Boolean withObject(Object value)
    {
        if (value == null) return null;
        else if (value instanceof Boolean) return (Boolean)value;
        else if (value instanceof Integer) return (Integer)value != 0;
        else if (value instanceof Short) return (Short)value != 0;
        else return null;
    }

    public static boolean withObject(Object value, boolean defaultValue)
    {
        if (value == null) return defaultValue;
        else if (value instanceof Boolean) return (Boolean)value;
        else if (value instanceof Integer) return (Integer)value != 0;
        else if (value instanceof Short) return (Short)value != 0;
        else return defaultValue;
    }
}
