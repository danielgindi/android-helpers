package com.dg.helpers

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

@Suppress("unused")
object JSONHelper
{
    fun toStringArray(json: JSONArray?): Array<String?>?
    {
        if (json == null) return null

        val length = json.length()
        val array = arrayOfNulls<String>(length)

        for (i in 0 until length)
        {
            val value = json.opt(i)
            array[i] = if (value == null) null else value as? String ?: value.toString()
        }

        return array
    }

    fun toArray(json: JSONArray?): ArrayList<*>?
    {
        if (json == null) return null

        val length = json.length()
        val array = ArrayList<Any?>(length)

        for (i in 0 until length)
        {
            val value = json.opt(i)
            when
            {
                value is JSONArray -> array.add(toArray(value))
                value === JSONObject.NULL -> array.add(null)
                value is JSONObject -> array.add(toMap(value))
                else -> array.add(value)
            }
        }

        return array
    }

    inline fun <reified T> toArray(list: List<*>): Array<T>
    {
        @Suppress("UNCHECKED_CAST")
        return (list as List<T>).toTypedArray()
    }

    inline fun <reified T> toArray(json: JSONArray?): Array<T>?
    {
        if (json == null) return null

        val length = json.length()
        val array = ArrayList<Any?>(length)

        for (i in 0 until length)
        {
            val value = json.opt(i)
            when
            {
                value is JSONArray -> array.add(toArray(value))
                value === JSONObject.NULL -> array.add(null)
                value is JSONObject -> array.add(toMap(value))
                else -> array.add(value)
            }
        }

        return toArray<T>(array)
    }

    fun toMap(json: JSONObject?): Map<String, Any?>?
    {
        if (json == null) return null

        val map = HashMap<String, Any?>(json.length())

        val keys = json.keys()
        while (keys.hasNext())
        {
            val name = keys.next()

            val value = json.opt(name)
            when
            {
                value is JSONArray -> map[name] = toArray(value)
                value === JSONObject.NULL -> map[name] = null
                value is JSONObject -> map[name] = toMap(value)
                else -> map[name] = value
            }
        }

        return map
    }

    /**
     * Wraps the given object if necessary.
     *
     *
     * If the object is null, returns `JSONObject.NULL`.
     * If the object is a `JSONArray` or `JSONObject`, no wrapping is necessary.
     * If the object is `JSONObject.NULL`, no wrapping is necessary.
     * If the object is an array or `Collection`, returns an equivalent `JSONArray`.
     * If the object is a `Map`, returns an equivalent `JSONObject`.
     * If the object is a primitive wrapper type or `String`, returns the object.
     * Otherwise if the object is from a `java` package, returns the result of `toString`.
     * If wrapping fails, returns null.
     */
    fun wrap(o: Any?): Any?
    {
        if (o == null)
        {
            return JSONObject.NULL
        }

        if (o is JSONArray || o is JSONObject)
        {
            return o
        }

        if (o == JSONObject.NULL)
        {
            return o
        }

        try
        {
            if (o is Collection<*>)
            {
                val jsonArray = JSONArray()
                for (entry in (o as Collection<*>?)!!)
                {
                    jsonArray.put(wrap(entry))
                }
                return jsonArray
            }

            if (o.javaClass.isArray)
            {
                val length = java.lang.reflect.Array.getLength(o)
                val jsonArray = JSONArray()
                for (i in 0 until length)
                {
                    jsonArray.put(wrap(java.lang.reflect.Array.get(o, i)))
                }

                return jsonArray
            }

            if (o is Map<*, *>)
            {
                val `object` = JSONObject()

                for ((key1, value) in o)
                {
                    val key = key1 as? String ?: continue

                    try
                    {
                        `object`.put(key, wrap(value))
                    }
                    catch (e: JSONException)
                    {
                        e.printStackTrace()
                    }

                }

                return `object`
            }

            if (o is Boolean ||
                    o is Byte ||
                    o is Char ||
                    o is Double ||
                    o is Float ||
                    o is Int ||
                    o is Long ||
                    o is Short ||
                    o is String)
            {
                return o
            }

            if (o.javaClass.getPackage().name.startsWith("java."))
            {
                return o.toString()
            }

        }
        catch (ignored: Exception)
        {
        }

        return null
    }
}