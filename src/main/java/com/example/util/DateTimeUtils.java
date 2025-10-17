package com.example.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class DateTimeUtils {

    private static final Pattern TIMEZONE_OFFSET_PATTERN = Pattern.compile(".*[+\\-]\\d{2}:?\\d{2}$");

    private DateTimeUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            // ISO 8601 with timezone Z
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"),
            // ISO 8601 with timezone offset
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxx"),
            // Basic ISO 8601
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            // With milliseconds and timezone
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
            // With basic milliseconds
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            // Simple format
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            // US format
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
            // European format
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            // Date only
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            // Time only
            DateTimeFormatter.ofPattern("HH:mm:ss"));

    /**
     * Parse datetime from various formats flexibly
     * 
     * @param dateTimeStr Datetime string to parse
     * @return Parsed LocalDateTime or null if input is null/empty
     * @throws IllegalArgumentException if format cannot be parsed
     */
    public static LocalDateTime parseFlexibleDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        // Try parsing with Instant first (handles ISO 8601 with Z and offset)
        if (dateTimeStr.endsWith("Z") || TIMEZONE_OFFSET_PATTERN.matcher(dateTimeStr).matches()) {
            try {
                return LocalDateTime.ofInstant(Instant.parse(dateTimeStr), ZoneOffset.UTC);
            } catch (DateTimeParseException e) {
                // Continue trying other formatters
            }
        }

        // Try parsing with formatters in order of priority
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(dateTimeStr, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }

        // If all formatters fail, try with Instant.parse() one last time
        try {
            return LocalDateTime.ofInstant(Instant.parse(dateTimeStr), ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Cannot parse datetime format: '" + dateTimeStr +
                    "'. Supported formats: ISO 8601, yyyy-MM-dd HH:mm:ss, MM/dd/yyyy HH:mm:ss, etc...", e);
        }
    }

    /**
     * Format LocalDateTime to standard ISO string
     * 
     * @param dateTime LocalDateTime to format
     * @return Formatted string or null if input is null
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Parse timestamp in epoch format (milliseconds)
     * 
     * @param timestamp Timestamp as long (milliseconds since epoch)
     * @return LocalDateTime from timestamp
     */
    public static LocalDateTime fromEpochMillis(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

    /**
     * Parse timestamp in epoch format (seconds)
     * 
     * @param timestamp Timestamp as long (seconds since epoch)
     * @return LocalDateTime from timestamp
     */
    public static LocalDateTime fromEpochSeconds(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
    }

    /**
     * Check if string is a valid datetime format
     * 
     * @param dateTimeStr String to check
     * @return true if can be parsed
     */
    public static boolean isValidDateTime(String dateTimeStr) {
        try {
            return parseFlexibleDateTime(dateTimeStr) != null;
        } catch (Exception e) {
            return false;
        }
    }
}