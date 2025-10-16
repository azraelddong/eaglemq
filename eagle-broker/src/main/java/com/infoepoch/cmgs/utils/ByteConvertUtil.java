package com.infoepoch.cmgs.utils;

public class ByteConvertUtil {

    public static byte[] intToByteArray(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static int byteArrayToInt(byte[] src) {
        return (src[0] & 0xFF)
                | ((src[1] << 8) & 0xFF00)
                | ((src[2] << 16) & 0xFF0000)
                | ((src[3] << 24) & 0xFF000000);
    }

    public static void main(String[] args) {
        int i = 10;
        byte[] content = intToByteArray(i);
        System.out.println(content.length); // 4

        int result = byteArrayToInt(content);
        System.out.println(result); //100
    }
}
