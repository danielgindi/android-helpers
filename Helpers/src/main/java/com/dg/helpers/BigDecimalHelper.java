package com.dg.helpers;

import java.math.BigDecimal;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class BigDecimalHelper
{
    public static BigDecimal withObject(Object value)
    {
        if (value == null) return null;
        else if (value instanceof BigDecimal) return (BigDecimal)value;
        else if (value instanceof Double) return new BigDecimal((Double)value);
        else if (value instanceof Float) return new BigDecimal((Float)value);
        else if (value instanceof Integer) return new BigDecimal((Integer)value);
        else if (value instanceof Short) return new BigDecimal((Short)value);
        else if (value instanceof String) return new BigDecimal((String)value);
        else return null;
    }
}
