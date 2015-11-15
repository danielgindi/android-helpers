package com.dg.helpers;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class DoubleHelper
{
    public static Double withObject(Object value)
    {
        if (value == null) return null;
        else if (value instanceof Double) return (Double)value;
        else if (value instanceof Float) return (double) (Float) value;
        else if (value instanceof Integer) return (double) (Integer) value;
        else if (value instanceof Short) return (double) (Short) value;
        else return null;
    }

    public static float[] toArray(Float[] inArray)
    {
        float[] array = new float[inArray.length];
        for (int i = 0, len = inArray.length; i < len; i++)
        {
            Float value = inArray[i];

            if (value != null)
            {
                array[i] = value;
            }
        }
        return array;
    }
}
