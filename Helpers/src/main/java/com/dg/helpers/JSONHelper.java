package com.dg.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
}