package com.example.hello.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Security utility - Security Manager APIs have been removed.
 * SecurityManager was deprecated in Java 17 (JEP 411) and removed in Java 24 (JEP 486).
 * This class now provides alternative security checks without using SecurityManager.
 */
public class SecurityUtil {

    private static final Logger logger = LogManager.getLogger(SecurityUtil.class);

    /**
     * Check if a SecurityManager is installed.
     * Note: SecurityManager has been removed. This method always returns false.
     */
    @Deprecated(since = "17", forRemoval = true)
    public static boolean isSecurityManagerEnabled() {
        // SecurityManager was removed in Java 24
        // Modern Java applications should use OS-level security, containers, or application-level access control
        return false;
    }

    /**
     * Check file read permission.
     * Note: This is a simplified check without SecurityManager.
     * For production use, consider OS-level permissions or application-level access control.
     */
    public static boolean canReadFile(String path) {
        try {
            Path filePath = Paths.get(path);
            return Files.isReadable(filePath);
        } catch (Exception e) {
            logger.warn("Cannot check read access for: " + path, e);
            return false;
        }
    }

    /**
     * Check network connect permission.
     * Note: SecurityManager has been removed. This method always returns true.
     * For production use, consider firewall rules or application-level network access control.
     */
    public static boolean canConnect(String host, int port) {
        // Without SecurityManager, there's no built-in Java-level network permission check
        // Use OS-level firewall rules or application-level access control instead
        logger.debug("Network access check requested for: " + host + ":" + port);
        return true;
    }
}
