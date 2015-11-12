package com.dg.helpers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.util.Base64;
import android.util.Log;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class TeaEncryptor
{

    static private List<Integer> bytesToLongs(byte[] s)
    {
        int slen = s.length;
        int len = (int) Math.ceil((slen) / 4.0d);
        List<Integer> l = new ArrayList<Integer>(len);
        int ll, lll;
        for (int i = 0; i < len; i++)
        {
            lll = 0;
            ll = i * 4;
            if (ll < slen) lll += s[ll];
            ll = i * 4 + 1;
            if (ll < slen) lll += s[ll] << 8;
            ll = i * 4 + 2;
            if (ll < slen) lll += s[ll] << 16;
            ll = i * 4 + 3;
            if (ll < slen) lll += s[ll] << 24;
            l.add(lll);
        }
        return l;
    }

    static private byte[] longsToBytes(List<Integer> l)
    {
        byte[] a = new byte[(l.size() * 4)];
        int ll;
        for (int i = 0, j = 0, len = l.size(); i < len; i++)
        {
            ll = l.get(i);
            a[j++] = ((byte) (ll & 0xFF));
            a[j++] = ((byte) (ll >> 8 & 0xFF));
            a[j++] = ((byte) (ll >> 16 & 0xFF));
            a[j++] = ((byte) (ll >> 24 & 0xFF));
        }
        return a;
    }

    static public String encrypt(String plaintext, String password)
    {
        if (plaintext.length() == 0) return "";
        List<Integer> v = bytesToLongs(plaintext.getBytes(Charset.forName("UTF-8")));
        while (v.size() <= 1) v.add(0);
        List<Integer> k = bytesToLongs(password.getBytes(Charset.forName("UTF-8")));
        while (k.size() < 4) k.add(0);
        int n = v.size();

        int z = v.get(n - 1), y = v.get(0), sum = 0, e, DELTA = (0x9e3779b9), mx;
        int q;
        q = 6 + 52 / n;
        while (q-- > 0)
        {
            sum += DELTA;
            e = sum >> 2 & 3;
            for (int p = 0; p < n; p++)
            {
                y = v.get((p + 1) % n);
                mx = (z >>> 5 ^ y << 2) + ((y >>> 3) ^ (z << 4)) ^ ((sum ^ y)) + (k.get((p & 3 ^ e)) ^ z);
                v.set(p, v.get(p) + mx);
                z = v.get(p);
            }
        }
        return Base64.encodeToString(longsToBytes(v), Base64.DEFAULT);
    }

    static public String decrypt(String ciphertext, String password)
    {
        if (ciphertext.length() == 0) return "";
        List<Integer> v = bytesToLongs(Base64.decode(ciphertext, Base64.DEFAULT));
        List<Integer> k = bytesToLongs(password.getBytes(Charset.forName("UTF-8")));
        while (k.size() < 4) k.add(0);
        int n = v.size();

        int z = v.get(n - 1), y = v.get(0), sum = 0, e, DELTA = (0x9e3779b9), mx;
        int q;
        q = 6 + 52 / n;
        sum = q * DELTA;

        while (sum != 0)
        {
            e = sum >> 2 & 3;
            for (int p = n - 1; p >= 0; p--)
            {
                z = v.get(p > 0 ? p - 1 : n - 1);
                mx = (z >>> 5 ^ y << 2) + ((y >>> 3) ^ (z << 4)) ^ ((sum ^ y)) + (k.get((p & 3 ^ e)) ^ z);
                v.set(p, v.get(p) - mx);
                y = v.get(p);
            }
            sum -= DELTA;
        }

        v.add(0); // null terminator
        byte[] plaintext = longsToBytes(v);
        try
        {
            return new String(plaintext, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            Log.i("HREQ", "Exception: " + ex.toString());
            return "";
        }
    }

}