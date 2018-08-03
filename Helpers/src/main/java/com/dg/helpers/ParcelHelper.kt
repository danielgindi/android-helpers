package com.dg.helpers

import android.os.Parcel

import java.util.Date

@Suppress("unused")
object ParcelHelper
{
    fun writeString(parcel: Parcel, value: String?)
    {
        parcel.writeByte(if (value == null) 0.toByte() else 1.toByte())
        if (value != null)
        {
            parcel.writeString(value)
        }
    }

    fun writeBoolean(parcel: Parcel, value: Boolean?)
    {
        parcel.writeByte(if (value == null) 0.toByte() else 1.toByte())
        if (value != null)
        {
            parcel.writeByte(if (value) 0.toByte() else 1.toByte())
        }
    }

    fun writeLong(parcel: Parcel, value: Long?)
    {
        parcel.writeByte(if (value == null) 0.toByte() else 1.toByte())
        if (value != null)
        {
            parcel.writeLong(value)
        }
    }

    fun writeInt(parcel: Parcel, value: Int?)
    {
        parcel.writeByte(if (value == null) 0.toByte() else 1.toByte())
        if (value != null)
        {
            parcel.writeInt(value)
        }
    }

    fun writeDouble(parcel: Parcel, value: Double?)
    {
        parcel.writeByte(if (value == null) 0.toByte() else 1.toByte())
        if (value != null)
        {
            parcel.writeDouble(value)
        }
    }

    fun writeFloat(parcel: Parcel, value: Float?)
    {
        parcel.writeByte(if (value == null) 0.toByte() else 1.toByte())
        if (value != null)
        {
            parcel.writeFloat(value)
        }
    }

    fun writeDate(parcel: Parcel, value: Date?)
    {
        parcel.writeByte(if (value == null) 0.toByte() else 1.toByte())
        if (value != null)
        {
            parcel.writeLong(value.time)
        }
    }

    fun readString(parcel: Parcel): String?
    {
        return if (parcel.readByte().toInt() == 0) null else parcel.readString()
    }

    fun readBoolean(parcel: Parcel): Boolean?
    {
        return if (parcel.readByte().toInt() == 0) null else parcel.readByte().toInt() == 1
    }

    fun readLong(parcel: Parcel): Long?
    {
        return if (parcel.readByte().toInt() == 0) null else parcel.readLong()
    }

    fun readInt(parcel: Parcel): Int?
    {
        return if (parcel.readByte().toInt() == 0) null else parcel.readInt()
    }

    fun readDouble(parcel: Parcel): Double?
    {
        return if (parcel.readByte().toInt() == 0) null else parcel.readDouble()
    }

    fun readFloat(parcel: Parcel): Float?
    {
        return if (parcel.readByte().toInt() == 0) null else parcel.readFloat()
    }

    fun readDate(parcel: Parcel): Date?
    {
        return if (parcel.readByte().toInt() == 0) null else Date(parcel.readLong())
    }
}
