package com.dg.helpers;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class ParcelHelper
{
    public static void writeString(Parcel parcel, String value)
    {
        parcel.writeByte(value == null ? (byte)0 : (byte)1);
        if (value != null)
        {
            parcel.writeString(value);
        }
    }

    public static void writeBoolean(Parcel parcel, Boolean value)
    {
        parcel.writeByte(value == null ? (byte)0 : (byte)1);
        if (value != null)
        {
            parcel.writeByte(value ? (byte) 0 : (byte) 1);
        }
    }

    public static void writeLong(Parcel parcel, Long value)
    {
        parcel.writeByte(value == null ? (byte)0 : (byte)1);
        if (value != null)
        {
            parcel.writeLong(value);
        }
    }

    public static void writeInt(Parcel parcel, Integer value)
    {
        parcel.writeByte(value == null ? (byte)0 : (byte)1);
        if (value != null)
        {
            parcel.writeInt(value);
        }
    }

    public static void writeDouble(Parcel parcel, Double value)
    {
        parcel.writeByte(value == null ? (byte)0 : (byte)1);
        if (value != null)
        {
            parcel.writeDouble(value);
        }
    }

    public static void writeFloat(Parcel parcel, Float value)
    {
        parcel.writeByte(value == null ? (byte)0 : (byte)1);
        if (value != null)
        {
            parcel.writeFloat(value);
        }
    }

    public static void writeDate(Parcel parcel, Date value)
    {
        parcel.writeByte(value == null ? (byte)0 : (byte)1);
        if (value != null)
        {
            parcel.writeLong(value.getTime());
        }
    }

    public static String readString(Parcel parcel)
    {
        return parcel.readByte() == 0 ? null : parcel.readString();
    }

    public static Boolean readBoolean(Parcel parcel)
    {
        return parcel.readByte() == 0 ? null : (parcel.readByte() == 1);
    }

    public static Long readLong(Parcel parcel)
    {
        return parcel.readByte() == 0 ? null : parcel.readLong();
    }

    public static Integer readInt(Parcel parcel)
    {
        return parcel.readByte() == 0 ? null : parcel.readInt();
    }

    public static Double readDouble(Parcel parcel)
    {
        return parcel.readByte() == 0 ? null : parcel.readDouble();
    }

    public static Float readFloat(Parcel parcel)
    {
        return parcel.readByte() == 0 ? null : parcel.readFloat();
    }

    public static Date readDate(Parcel parcel)
    {
        return parcel.readByte() == 0 ? null : new Date(parcel.readLong());
    }
}
