package com.dg.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DbHelper
{
    public static SimpleDateFormat utcIsoDateFormatter()
    {
        return DateHelper.utcIsoDateFormatter();
    }

    public static String getDateForDbFromDate(Date date, boolean escape)
    {
        if (date != null)
        {
            if (escape)
            {
                return "'" + DateHelper.utcIsoDateFormatter().format(date) + "'";
            }
            else
            {
                return DateHelper.utcIsoDateFormatter().format(date);
            }
        }
        else
        {
            if (escape)
            {
                return "null";
            }
            else
            {
                return null;
            }
        }
    }

    public static Date getDateFromDbString(String date)
    {
        if (date == null)
        {
            return null;
        }
        try
        {
            return DateHelper.utcIsoDateFormatter().parse(date);
        }
        catch (ParseException ignored)
        {
            return null;
        }
    }

    public static Float queryScalarFloat(SQLiteDatabase database, String sql)
    {
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            Float val = cursor.isNull(0) ? null : cursor.getFloat(0);
            cursor.close();
            return val;
        }
        cursor.close();
        return null;
    }

    public static Integer queryScalarInt(SQLiteDatabase database, String sql)
    {
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            Integer val = cursor.isNull(0) ? null : cursor.getInt(0);
            cursor.close();
            return val;
        }
        cursor.close();
        return null;
    }

    public static Long queryScalarLong(SQLiteDatabase database, String sql)
    {
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            Long val = cursor.isNull(0) ? null : cursor.getLong(0);
            cursor.close();
            return val;
        }
        cursor.close();
        return null;
    }

    public static Double queryScalarDouble(SQLiteDatabase database, String sql)
    {
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            Double val = cursor.isNull(0) ? null : cursor.getDouble(0);
            cursor.close();
            return val;
        }
        cursor.close();
        return null;
    }

    public static Short queryScalarShort(SQLiteDatabase database, String sql)
    {
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            Short val = cursor.isNull(0) ? null : cursor.getShort(0);
            cursor.close();
            return val;
        }
        cursor.close();
        return null;
    }

    public static Boolean queryScalarBoolean(SQLiteDatabase database, String sql)
    {
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            Boolean val = cursor.isNull(0) ? null : cursor.getInt(0) != 0;
            cursor.close();
            return val;
        }
        cursor.close();
        return null;
    }

    public static String queryScalarString(SQLiteDatabase database, String sql)
    {
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            String val = cursor.isNull(0) ? null : cursor.getString(0);
            cursor.close();
            return val;
        }
        cursor.close();
        return null;
    }

    public static ContentValues getContentValuesForMap(Map<String, ?> values)
    {
        ContentValues content = new ContentValues();

        for (String key : values.keySet())
        {
            Object value = values.get(key);
            if (value == null)
            {
                content.putNull(key);
            }
            else if (value instanceof Integer)
            {
                content.put(key, (Integer)value);
            }
            else if (value instanceof Short)
            {
                content.put(key, (Short)value);
            }
            else if (value instanceof Long)
            {
                content.put(key, (Long)value);
            }
            else if (value instanceof Float)
            {
                content.put(key, (Float)value);
            }
            else if (value instanceof Double)
            {
                content.put(key, (Double)value);
            }
            else if (value instanceof Boolean)
            {
                content.put(key, (Boolean)value);
            }
            else if (value instanceof String)
            {
                content.put(key, (String)value);
            }
            else if (value instanceof Date)
            {
                content.put(key, getDateForDbFromDate((Date) value, false));
            }
        }

        return content;
    }

    public static String escape(String value)
    {
        return value.replace("'", "''");
    }
}
