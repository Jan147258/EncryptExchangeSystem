package com.jan.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * 加密工具类
 * 提供字节数组与Base64字符串的互转功能，简化密文传输与存储
 */
public class EncryptUtil {
    /**
     * 字节数组转Base64字符串
     */
    public static String byteToBase64(byte[] bytes) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    /**
     * Base64字符串转字节数组
     */
    public static byte[] base64ToByte(String base64Str) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Str);
    }
}