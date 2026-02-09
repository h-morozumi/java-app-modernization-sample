package com.example.hello.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Encoding utility using sun.misc.BASE64Encoder/Decoder via reflection.
 * These internal APIs were removed in Java 11+.
 * Should be migrated to java.util.Base64.
 *
 * Reflection is used to bypass webapp classloader restrictions in embedded Tomcat,
 * while still exercising the actual sun.misc classes from the JDK.
 */
public class EncodingUtil {

    private static final Logger logger = Logger.getLogger(EncodingUtil.class);

    /**
     * Encode bytes to Base64 string using sun.misc.BASE64Encoder (removed in Java 11).
     * Loaded via system classloader to work in embedded Tomcat.
     */
    public static String encodeBase64(byte[] data) throws Exception {
        // sun.misc.BASE64Encoder - removed in Java 11
        Class<?> encoderClass = ClassLoader.getSystemClassLoader().loadClass("sun.misc.BASE64Encoder");
        Object encoder = encoderClass.newInstance();
        Method encodeMethod = encoderClass.getMethod("encode", byte[].class);
        String encoded = (String) encodeMethod.invoke(encoder, data);
        logger.debug("Encoded " + data.length + " bytes to Base64");
        return encoded;
    }

    /**
     * Decode Base64 string to bytes using sun.misc.BASE64Decoder (removed in Java 11).
     * Loaded via system classloader to work in embedded Tomcat.
     */
    public static byte[] decodeBase64(String data) throws Exception {
        // sun.misc.BASE64Decoder - removed in Java 11
        Class<?> decoderClass = ClassLoader.getSystemClassLoader().loadClass("sun.misc.BASE64Decoder");
        Object decoder = decoderClass.newInstance();
        Method decodeMethod = decoderClass.getMethod("decodeBuffer", String.class);
        byte[] decoded = (byte[]) decodeMethod.invoke(decoder, data);
        logger.debug("Decoded Base64 string to " + decoded.length + " bytes");
        return decoded;
    }

    /**
     * Encode a string to Base64.
     */
    public static String encodeString(String text) throws Exception {
        return encodeBase64(text.getBytes());
    }

    /**
     * Decode a Base64 string.
     */
    public static String decodeString(String base64) throws Exception {
        return new String(decodeBase64(base64));
    }
}
