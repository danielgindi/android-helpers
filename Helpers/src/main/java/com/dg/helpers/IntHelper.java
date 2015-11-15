package com.dg.helpers;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class IntHelper
{
    public static Integer withObject(Object value)
    {
        if (value == null) return null;
        else if (value instanceof Integer) return (Integer)value;
        else if (value instanceof Short) return (int) (Short) value;
        else return null;
    }

    public static int[] toArray(Integer[] inArray)
    {
        int[] array = new int[inArray.length];
        for (int i = 0, len = inArray.length; i < len; i++)
        {
            Integer value = inArray[i];

            if (value != null)
            {
                array[i] = value;
            }
        }
        return array;
    }
}
