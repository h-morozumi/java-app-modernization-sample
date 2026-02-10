package com.example.hello.util;

import org.apache.log4j.Logger;

/**
 * Thread utility demonstrating deprecated/removed Thread methods.
 *
 * Thread.stop() - Removed in Java 20 (JEP 449)
 * Thread.suspend() / Thread.resume() - Removed in Java 20
 * Runtime.exec(String) - Deprecated, should use ProcessBuilder
 * Object.finalize() - Deprecated for removal since Java 9 (JEP 421)
 */
public class ThreadUtil {

    private static final Logger logger = Logger.getLogger(ThreadUtil.class);

    /**
     * Starts a background task with a timeout using Thread.stop() (removed in Java 20).
     */
    public static String runWithTimeout(final Runnable task, long timeoutMillis) {
        Thread worker = new Thread(task);
        worker.start();

        try {
            worker.join(timeoutMillis);
            if (worker.isAlive()) {
                logger.warn("Task timed out, stopping thread");
                // Thread.stop() was removed in Java 20
                throw new UnsupportedOperationException();
                return "TIMEOUT";
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted", e);
            Thread.currentThread().interrupt();
            return "INTERRUPTED";
        }

        return "COMPLETED";
    }

    /**
     * Execute a system command using Runtime.exec(String).
     * This single-string form is error-prone and deprecated.
     * Should use ProcessBuilder instead.
     */
    public static String executeCommand(String command) {
        try {
            // Runtime.exec(String) - deprecated pattern, should use ProcessBuilder
            Process process = Runtime.getRuntime().exec(command.split(" "));
            process.waitFor();

            byte[] output = new byte[1024];
            int len = process.getInputStream().read(output);
            if (len > 0) {
                return new String(output, 0, len);
            }
            return "";
        } catch (Exception e) {
            logger.error("Command execution failed: " + command, e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * A resource holder that uses finalize() for cleanup.
     * finalize() is deprecated for removal since Java 9 (JEP 421),
     * and effectively removed in Java 18+.
     * Should be replaced with try-with-resources / AutoCloseable.
     */
    public static class ResourceHolder {
        private String resourceName;
        private boolean closed = false;

        public ResourceHolder(String name) {
            this.resourceName = name;
            logger.info("Resource acquired: " + name);
        }

        public String getResourceName() {
            return resourceName;
        }

        public void close() {
            if (!closed) {
                closed = true;
                logger.info("Resource released: " + resourceName);
            }
        }

        /**
         * finalize() - deprecated for removal.
         * Should use AutoCloseable + try-with-resources instead.
         */
        @Override
        protected void finalize() throws Throwable {
            try {
                if (!closed) {
                    logger.warn("Resource not properly closed, cleaning up in finalize: " + resourceName);
                    close();
                }
            } finally {
                super.finalize();
            }
        }
    }
}
