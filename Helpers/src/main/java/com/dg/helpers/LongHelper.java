package com.dg.helpers;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class LongHelper
{
    public static Long withObject(Object value)
    {
        if (value == null) return null;
        else if (value instanceof Long) return (Long)value;
        else if (value instanceof Integer) return (long) (Integer) value;
        else if (value instanceof Short) return (long) (Short) value;
        else return null;
    }
}
