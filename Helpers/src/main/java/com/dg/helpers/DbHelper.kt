package com.dg.helpers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
object DbHelper
{
    fun utcIsoDateFormatter(): SimpleDateFormat
    {
        return DateHelper.utcIsoDateFormatter()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getDateForDbFromDate(date: Date?, escape: Boolean): String?
    {
        return if (date != null)
        {
            if (escape)
            {
                "'" + DateHelper.utcIsoDateFormatter().format(date) + "'"
            }
            else
            {
                DateHelper.utcIsoDateFormatter().format(date)
            }
        }
        else
        {
            if (escape)
            {
                "null"
            }
            else
            {
                null
            }
        }
    }

    fun getDateFromDbString(date: String?): Date?
    {
        if (date == null)
        {
            return null
        }
        try
        {
            return DateHelper.utcIsoDateFormatter().parse(date)
        }
        catch (ignored: ParseException)
        {
            return null
        }

    }

    fun queryScalarFloat(database: SQLiteDatabase, sql: String): Float?
    {
        val cursor = database.rawQuery(sql, null)
        if (cursor.moveToFirst())
        {
            val `val` = if (cursor.isNull(0)) null else cursor.getFloat(0)
            cursor.close()
            return `val`
        }
        cursor.close()
        return null
    }

    fun queryScalarInt(database: SQLiteDatabase, sql: String): Int?
    {
        val cursor = database.rawQuery(sql, null)
        if (cursor.moveToFirst())
        {
            val `val` = if (cursor.isNull(0)) null else cursor.getInt(0)
            cursor.close()
            return `val`
        }
        cursor.close()
        return null
    }

    fun queryScalarLong(database: SQLiteDatabase, sql: String): Long?
    {
        val cursor = database.rawQuery(sql, null)
        if (cursor.moveToFirst())
        {
            val `val` = if (cursor.isNull(0)) null else cursor.getLong(0)
            cursor.close()
            return `val`
        }
        cursor.close()
        return null
    }

    fun queryScalarDouble(database: SQLiteDatabase, sql: String): Double?
    {
        val cursor = database.rawQuery(sql, null)
        if (cursor.moveToFirst())
        {
            val `val` = if (cursor.isNull(0)) null else cursor.getDouble(0)
            cursor.close()
            return `val`
        }
        cursor.close()
        return null
    }

    fun queryScalarShort(database: SQLiteDatabase, sql: String): Short?
    {
        val cursor = database.rawQuery(sql, null)
        if (cursor.moveToFirst())
        {
            val `val` = if (cursor.isNull(0)) null else cursor.getShort(0)
            cursor.close()
            return `val`
        }
        cursor.close()
        return null
    }

    fun queryScalarBoolean(database: SQLiteDatabase, sql: String): Boolean?
    {
        val cursor = database.rawQuery(sql, null)
        if (cursor.moveToFirst())
        {
            val `val` = if (cursor.isNull(0)) null else cursor.getInt(0) != 0
            cursor.close()
            return `val`
        }
        cursor.close()
        return null
    }

    fun queryScalarString(database: SQLiteDatabase, sql: String): String?
    {
        val cursor = database.rawQuery(sql, null)
        if (cursor.moveToFirst())
        {
            val `val` = if (cursor.isNull(0)) null else cursor.getString(0)
            cursor.close()
            return `val`
        }
        cursor.close()
        return null
    }

    fun getContentValuesForMap(values: Map<String, *>): ContentValues
    {
        val content = ContentValues()

        for (key in values.keys)
        {
            when (val value = values[key])
            {
                null -> content.putNull(key)
                is Int -> content.put(key, value)
                is Short -> content.put(key, value)
                is Long -> content.put(key, value)
                is Float -> content.put(key, value)
                is Double -> content.put(key, value)
                is Boolean -> content.put(key, value)
                is String -> content.put(key, value)
                is Date -> content.put(key, getDateForDbFromDate(value, false))
            }
        }

        return content
    }

    fun escape(value: String): String
    {
        return value.replace("'", "''")
    }
}
