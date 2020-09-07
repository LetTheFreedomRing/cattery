package com.example.cattery;

public class Utils {
    public static Byte[] convert(byte[] bytes) {
        Byte[] res = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            res[i] = bytes[i];
        }
        return res;
    }
}
