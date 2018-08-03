package com.dg.helpers

import org.json.JSONObject

@Suppress("unused")
object ObjectHelper
{
    private val JSON_NULL = JSONObject.NULL

    /**
     * Checks for equality between two objects.
     * * Normalizes `JSONObject.NULL` to null
     * * Successfully compares Float with Double
     * * Successfully compares Integer with Long
     * @param left
     * @param right
     * @return true if equal
     */
    fun equals(left: Any?, right: Any?): Boolean
    {
        var left = left
        var right = right
        if (left === JSON_NULL)
        {
            left = null
        }

        if (right === JSON_NULL)
        {
            right = null
        }

        if (left == null && right != null)
        {
            return false
        }

        if (right == null && left != null)
        {
            return false
        }

        if (left == null)
        {
            return true
        }

        if (left is Float && right is Double)
        {
            return right == left.toDouble()
        }

        if (right is Float && left is Double)
        {
            return left == right.toDouble()
        }

        if (left is Int && right is Long)
        {
            return right == left.toLong()
        }

        return if (right is Int && left is Long)
        {
            left == right.toLong()
        }
        else left == right
    }
}
