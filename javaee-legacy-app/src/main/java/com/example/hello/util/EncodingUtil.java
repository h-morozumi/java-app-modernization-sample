package com.example.hello.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;

/**
 * Encoding utility using java.util.Base64 (modern replacement for sun.misc.BASE64Encoder).
 * sun.misc.BASE64Encoder/Decoder were removed in Java 11+.
 * Migrated to java.util.Base64 (available since Java 8).
 */
public class EncodingUtil {

    private static final Logger logger = LogManager.getLogger(EncodingUtil.class);

    /**
     * Encode bytes to Base64 string using java.util.Base64.
     */
    public static String encodeBase64(byte[] data) {
        String encoded = Base64.getEncoder().encodeToString(data);
        logger.debug("Encoded " + data.length + " bytes to Base64");
        return encoded;
    }

    /**
     * Decode Base64 string to bytes using java.util.Base64.
     */
    public static byte[] decodeBase64(String data) {
        byte[] decoded = Base64.getDecoder().decode(data);
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
    public static String decodeString(String base64) {
        return new String(decodeBase64(base64));
    }
}
