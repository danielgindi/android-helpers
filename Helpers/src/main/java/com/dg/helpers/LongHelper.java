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
        else if (value instanceof String)
        {
            try { return Long.parseLong((String) value); }
            catch (Exception ignored) { }
        }
        return null;
    }

    public static Long withObject(Object value, long defaultValue)
    {
        if (value == null) return defaultValue;
        else if (value instanceof Long) return (Long)value;
        else if (value instanceof Integer) return (long) (Integer) value;
        else if (value instanceof Short) return (long) (Short) value;
        else if (value instanceof String)
        {
            try { return Long.parseLong((String) value); }
            catch (Exception ignored) { }
        }
        return defaultValue;
    }

    public static long[] toArray(Long[] inArray)
    {
        long[] array = new long[inArray.length];
        for (int i = 0, len = inArray.length; i < len; i++)
        {
            Long value = inArray[i];

            if (value != null)
            {
                array[i] = value;
            }
        }
        return array;
    }
}
