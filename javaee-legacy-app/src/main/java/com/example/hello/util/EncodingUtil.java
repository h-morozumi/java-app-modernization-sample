package com.example.hello.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Encoding utility using sun.misc.BASE64Encoder/Decoder.
 * These internal APIs were removed in Java 11+.
 * Should be migrated to java.util.Base64.
 */
public class EncodingUtil {

    private static final Logger logger = Logger.getLogger(EncodingUtil.class);

    /**
     * Encode bytes to Base64 string using sun.misc.BASE64Encoder (removed in Java 11).
     */
    public static String encodeBase64(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encoded = encoder.encode(data);
        logger.debug("Encoded " + data.length + " bytes to Base64");
        return encoded;
    }

    /**
     * Decode Base64 string to bytes using sun.misc.BASE64Decoder (removed in Java 11).
     */
    public static byte[] decodeBase64(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decoded = decoder.decodeBuffer(data);
        logger.debug("Decoded Base64 string to " + decoded.length + " bytes");
        return decoded;
    }

    /**
     * Encode a string to Base64.
     */
    public static String encodeString(String text) {
        return encodeBase64(text.getBytes());
    }

    /**
     * Decode a Base64 string.
     */
    public static String decodeString(String base64) throws IOException {
        return new String(decodeBase64(base64));
    }
}
