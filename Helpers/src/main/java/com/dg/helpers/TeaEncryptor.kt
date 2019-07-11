package com.dg.helpers

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.ArrayList

import android.util.Base64
import android.util.Log
import kotlin.math.ceil

@Suppress("unused")
object TeaEncryptor
{
    private fun bytesToLongs(s: ByteArray): MutableList<Int>
    {
        val slen = s.size
        val len = ceil(slen / 4.0).toInt()
        val l = ArrayList<Int>(len)
        var ll: Int
        var lll: Int
        for (i in 0 until len)
        {
            lll = 0
            ll = i * 4
            if (ll < slen) lll += s[ll].toInt()
            ll = i * 4 + 1
            if (ll < slen) lll += s[ll].toInt() shl 8
            ll = i * 4 + 2
            if (ll < slen) lll += s[ll].toInt() shl 16
            ll = i * 4 + 3
            if (ll < slen) lll += s[ll].toInt() shl 24
            l.add(lll)
        }
        return l
    }

    private fun longsToBytes(l: List<Int>): ByteArray
    {
        val a = ByteArray(l.size * 4)
        var ll: Int
        var i = 0
        var j = 0
        val len = l.size
        while (i < len)
        {
            ll = l[i]
            a[j++] = (ll and 0xFF).toByte()
            a[j++] = (ll shr 8 and 0xFF).toByte()
            a[j++] = (ll shr 16 and 0xFF).toByte()
            a[j++] = (ll shr 24 and 0xFF).toByte()
            i++
        }
        return a
    }

    private const val DELTA = -0x61c88647

    fun encrypt(plaintext: String, password: String): String
    {
        if (plaintext.isEmpty()) return ""
        val v = bytesToLongs(plaintext.toByteArray(Charset.forName("UTF-8")))
        while (v.size <= 1) v.add(0)
        val k = bytesToLongs(password.toByteArray(Charset.forName("UTF-8")))
        while (k.size < 4) k.add(0)
        val n = v.size

        var z = v[n - 1]
        var y: Int
        var sum = 0
        var e: Int
        var mx: Int
        var q: Int
        q = 6 + 52 / n
        while (q-- > 0)
        {
            sum += DELTA
            e = sum shr 2 and 3
            for (p in 0 until n)
            {
                y = v[(p + 1) % n]
                mx = (z.ushr(5) xor (y shl 2)) + (y.ushr(3) xor (z shl 4)) xor (sum xor y) + (k[p and 3 xor e] xor z)
                v[p] = v[p] + mx
                z = v[p]
            }
        }
        return Base64.encodeToString(longsToBytes(v), Base64.DEFAULT)
    }

    fun decrypt(ciphertext: String, password: String): String
    {
        if (ciphertext.isEmpty()) return ""
        val v = bytesToLongs(Base64.decode(ciphertext, Base64.DEFAULT))
        val k = bytesToLongs(password.toByteArray(Charset.forName("UTF-8")))
        while (k.size < 4) k.add(0)
        val n = v.size

        var z: Int
        var y = v[0]
        var sum: Int
        var e: Int
        var mx: Int
        val q: Int
        q = 6 + 52 / n
        sum = q * DELTA

        while (sum != 0)
        {
            e = sum shr 2 and 3
            for (p in n - 1 downTo 0)
            {
                z = v[if (p > 0) p - 1 else n - 1]
                mx = (z.ushr(5) xor (y shl 2)) + (y.ushr(3) xor (z shl 4)) xor (sum xor y) + (k[p and 3 xor e] xor z)
                v[p] = v[p] - mx
                y = v[p]
            }
            sum -= DELTA
        }

        v.add(0) // null terminator
        val plaintext = longsToBytes(v)
        try
        {
            return String(plaintext, Charset.forName("UTF-8"))
        }
        catch (ex: UnsupportedEncodingException)
        {
            Log.i("HREQ", "Exception: $ex")
            return ""
        }

    }

}