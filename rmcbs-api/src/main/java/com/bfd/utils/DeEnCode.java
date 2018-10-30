package com.bfd.utils;

import java.nio.charset.Charset;

public class DeEnCode {
    
    private static final Charset charset = Charset.forName("UTF-8");
    
    public static String encode(String enc, String key) {
        byte[] b = enc.getBytes(charset);
        byte[] keyBytes = key.getBytes(charset);
        for (int i = 0, size = b.length; i < size; i++) {
            for (byte keyBytes0 : keyBytes) {
                b[i] = (byte)(b[i] ^ keyBytes0);
            }
        }
        return byte2hex(b);
    }
    
    public static String decode(String dec, String key) {
        byte[] e = hex2byte(dec);
        byte[] keyBytes = key.getBytes(charset);
        byte[] dee = e;
        for (int i = 0, size = e.length; i < size; i++) {
            for (byte keyBytes0 : keyBytes) {
                e[i] = (byte)(dee[i] ^ keyBytes0);
            }
        }
        return new String(e);
    }
    
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte)Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }
    
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
}