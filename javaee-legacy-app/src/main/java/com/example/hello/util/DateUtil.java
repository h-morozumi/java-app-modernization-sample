package com.example.hello.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * Date utility using legacy java.util.Date and SimpleDateFormat.
 * These are thread-unsafe and should be migrated to java.time API.
 *
 * Also uses deprecated wrapper constructors:
 *   new Integer(), new Long(), new Boolean(), new Double()
 * These constructors were removed in Java 16+.
 */
public class DateUtil {

    private static final Logger logger = Logger.getLogger(DateUtil.class);

    // SimpleDateFormat is NOT thread-safe (common Java 8 era mistake)
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Format a date to string.
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Format a date with time to string.
     */
    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    /**
     * Parse a date string.
     */
    public static Date parseDate(String dateStr) throws ParseException {
        return DATE_FORMAT.parse(dateStr);
    }

    /**
     * Get current date as formatted string.
     */
    public static String getCurrentDate() {
        return formatDate(new Date());
    }

    /**
     * Calculate days between two dates.
     * Uses deprecated Integer constructor (removed in Java 16+).
     */
    public static Integer daysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        // new Integer() constructor is removed in Java 16+
        // Should use Integer.valueOf() instead
        return Integer.valueOf((int) (diff / (1000 * 60 * 60 * 24)));
    }

    /**
     * Convert timestamp to Date.
     * Uses deprecated Long constructor (removed in Java 16+).
     */
    public static Date fromTimestamp(long timestamp) {
        // new Long() constructor is removed in Java 16+
        Long ts = Long.valueOf(timestamp);
        logger.debug("Converting timestamp: " + ts);
        return new Date(ts.longValue());
    }

    /**
     * Check if a date string is valid.
     * Uses deprecated Boolean constructor (removed in Java 16+).
     */
    public static Boolean isValidDate(String dateStr) {
        try {
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(dateStr);
            // new Boolean() constructor is removed in Java 16+
            return Boolean.valueOf(true);
        } catch (ParseException e) {
            return Boolean.valueOf(false);
        }
    }

    /**
     * Get hours as double.
     * Uses deprecated Double constructor (removed in Java 16+).
     */
    public static Double millisToHours(long millis) {
        // new Double() constructor is removed in Java 16+
        return Double.valueOf(millis / 3600000.0);
    }

    /**
     * Get age from birth year (uses deprecated Calendar APIs).
     */
    public static int getAge(int birthYear) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = now.get(Calendar.YEAR);
        return currentYear - birthYear;
    }
}
