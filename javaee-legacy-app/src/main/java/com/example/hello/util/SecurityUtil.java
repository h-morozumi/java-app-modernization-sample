package com.example.hello.util;

import org.apache.log4j.Logger;

/**
 * Security utility demonstrating usage of SecurityManager.
 * SecurityManager was deprecated in Java 17 (JEP 411) and removed in Java 24 (JEP 486).
 */
public class SecurityUtil {

    private static final Logger logger = Logger.getLogger(SecurityUtil.class);

    /**
     * Check if a SecurityManager is installed (removed in Java 24).
     */
    public static boolean isSecurityManagerEnabled() {
        // System.getSecurityManager() removed in Java 24
        SecurityManager sm = System.getSecurityManager();
        return sm != null;
    }

    /**
     * Check file read permission using SecurityManager (removed in Java 24).
     */
    public static boolean canReadFile(String path) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                sm.checkRead(path);
                return true;
            } catch (SecurityException e) {
                logger.warn("Read access denied: " + path);
                return false;
            }
        }
        // No SecurityManager = no restrictions
        return true;
    }

    /**
     * Check network connect permission using SecurityManager (removed in Java 24).
     */
    public static boolean canConnect(String host, int port) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                sm.checkConnect(host, port);
                return true;
            } catch (SecurityException e) {
                logger.warn("Connect denied: " + host + ":" + port);
                return false;
            }
        }
        return true;
    }
}
