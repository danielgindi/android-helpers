package com.dg.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class JSONHelper
{
    public static String[] toStringArray(JSONArray json)
    {
        if (json == null) return null;

        int length = json.length();
        String[] array = new String[length];

        for (int i = 0; i < length; i++)
        {
            Object value = json.opt(i);
            array[i] = value == null ? null : value instanceof String ? (String)value : String.valueOf(value);
        }

        return array;
    }

    public static ArrayList toArray(JSONArray json)
    {
        if (json == null) return null;

        int length = json.length();
        ArrayList<Object> array = new ArrayList<>(length);

        for (int i = 0; i < length; i++)
        {
            Object value = json.opt(i);
            if (value instanceof JSONArray)
            {
                array.add(toArray((JSONArray)value));
            }
            else if (value == JSONObject.NULL)
            {
                array.add(null);
            }
            else if (value instanceof JSONObject)
            {
                array.add(toMap((JSONObject) value));
            }
            else
            {
                array.add(value);
            }
        }

        return array;
    }

    public static <T> T[] toArray(JSONArray json, Class<T> typeClass)
    {
        if (json == null) return null;

        int length = json.length();
        ArrayList<Object> array = new ArrayList<>(length);

        for (int i = 0; i < length; i++)
        {
            Object value = json.opt(i);
            if (value instanceof JSONArray)
            {
                array.add(toArray((JSONArray)value));
            }
            else if (value == JSONObject.NULL)
            {
                array.add(null);
            }
            else if (value instanceof JSONObject)
            {
                array.add(toMap((JSONObject) value));
            }
            else
            {
                array.add(value);
            }
        }

        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(typeClass, length);
        return array.toArray(result);
    }

    public static Map<String, Object> toMap(JSONObject json)
    {
        if (json == null) return null;

        Map<String, Object> map = new HashMap<>(json.length());

        Iterator<String> keys = json.keys();
        while (keys.hasNext())
        {
            String name = keys.next();

            Object value = json.opt(name);
            if (value instanceof JSONArray)
            {
                map.put(name, toArray((JSONArray) value));
            }
            else if (value == JSONObject.NULL)
            {
                map.put(name, null);
            }
            else if (value instanceof JSONObject)
            {
                map.put(name, toMap((JSONObject) value));
            }
            else
            {
                map.put(name, value);
            }
        }

        return map;
    }

    /**
     * Wraps the given object if necessary.
     *
     * <p>If the object is null, returns {@code JSONObject.NULL}.
     * If the object is a {@code JSONArray} or {@code JSONObject}, no wrapping is necessary.
     * If the object is {@code JSONObject.NULL}, no wrapping is necessary.
     * If the object is an array or {@code Collection}, returns an equivalent {@code JSONArray}.
     * If the object is a {@code Map}, returns an equivalent {@code JSONObject}.
     * If the object is a primitive wrapper type or {@code String}, returns the object.
     * Otherwise if the object is from a {@code java} package, returns the result of {@code toString}.
     * If wrapping fails, returns null.
     */
    public static Object wrap(Object o)
    {
        if (o == null)
        {
            return JSONObject.NULL;
        }

        if (o instanceof JSONArray || o instanceof JSONObject)
        {
            return o;
        }

        if (o.equals(JSONObject.NULL))
        {
            return o;
        }

        try
        {
            if (o instanceof Collection)
            {
                JSONArray jsonArray = new JSONArray();
                for (Object entry : (Collection)o)
                {
                    jsonArray.put(wrap(entry));
                }
                return jsonArray;
            }

            if (o.getClass().isArray())
            {
                final int length = Array.getLength(o);
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < length; ++i)
                {
                    jsonArray.put(wrap(Array.get(o, i)));
                }

                return jsonArray;
            }

            if (o instanceof Map)
            {
                JSONObject object = new JSONObject();

                for (Map.Entry<?, ?> entry : ((Map<?, ?>)o).entrySet())
                {
                    String key = (String) entry.getKey();

                    if (key == null)
                    {
                        continue;
                    }

                    try
                    {
                        object.put(key, wrap(entry.getValue()));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                return object;
            }

            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }

            if (o.getClass().getPackage().getName().startsWith("java."))
            {
                return o.toString();
            }

        }
        catch (Exception ignored)
        {
        }
        return null;
    }
}