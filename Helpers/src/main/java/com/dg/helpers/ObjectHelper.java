package com.dg.helpers;

import org.json.JSONObject;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class ObjectHelper
{
    private static Object JSONNULL = JSONObject.NULL;

    /**
     * Checks for equality between two objects.
     * * Normalizes {@code JSONObject.NULL} to null
     * * Successfully compares Float with Double
     * * Successfully compares Integer with Long
     * @param left
     * @param right
     * @return true if equal
     */
    public static boolean equals(Object left, Object right)
    {
        if (left == JSONNULL)
        {
            left = null;
        }

        if (right == JSONNULL)
        {
            right = null;
        }

        if (left == null && right != null)
        {
            return false;
        }

        if (right == null && left != null)
        {
            return false;
        }

        if (left == null)
        {
            return true;
        }

        if (left instanceof Float && right instanceof Double)
        {
            return right.equals((double)(float)(Float)left);
        }

        if (right instanceof Float && left instanceof Double)
        {
            return left.equals((double)(float)(Float)right);
        }

        if (left instanceof Integer && right instanceof Long)
        {
            return right.equals((long)(int)(Integer)left);
        }

        if (right instanceof Integer && left instanceof Long)
        {
            return left.equals((long)(int)(Integer)right);
        }

        return left.equals(right);
    }
}
