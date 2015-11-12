package com.dg.helpers;

import java.util.Collection;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class CollectionHelper
{
    public static boolean isEmpty(Collection array)
    {
        return array == null || array.isEmpty();
    }

    public static boolean isEmpty(Object[] array)
    {
        return array == null || array.length == 0;
    }
}
