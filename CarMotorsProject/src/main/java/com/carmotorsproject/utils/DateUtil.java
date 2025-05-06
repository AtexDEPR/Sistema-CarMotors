package com.carmotorsproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides utility methods for date handling and formatting.
 */
public class DateUtil {
    private static final Logger LOGGER = Logger.getLogger(DateUtil.class.getName());

    // Common date formats
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Formats a Date object to a string using the specified pattern.
     *
     * @param date The date to format
     * @param pattern The pattern to use for formatting
     * @return The formatted date string, or null if the date is null
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * Formats a Date object using the default date format (dd/MM/yyyy).
     *
     * @param date The date to format
     * @return The formatted date string, or null if the date is null
     */
    public static String formatDate(Date date) {
        return formatDate(date, DATE_FORMAT);
    }

    /**
     * Parses a date string to a Date object using the specified pattern.
     *
     * @param dateStr The date string to parse
     * @param pattern The pattern to use for parsing
     * @return The parsed Date object, or null if parsing fails
     */
    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            formatter.setLenient(false); // Strict parsing
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing date: {0} with pattern: {1}", new Object[]{dateStr, pattern});
            return null;
        }
    }

    /**
     * Parses a date string using the default date format (dd/MM/yyyy).
     *
     * @param dateStr The date string to parse
     * @return The parsed Date object, or null if parsing fails
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DATE_FORMAT);
    }

    /**
     * Calculates the number of days between two dates.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return The number of days between the dates, or -1 if either date is null
     */
    public static long daysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }

        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Adds a specified number of days to a date.
     *
     * @param date The date to add days to
     * @param days The number of days to add
     * @return The new date with days added, or null if the input date is null
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newDate = localDate.plusDays(days);

        return Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Checks if a date is before the current date.
     *
     * @param date The date to check
     * @return True if the date is before the current date, false otherwise
     */
    public static boolean isBeforeToday(Date date) {
        if (date == null) {
            return false;
        }

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();

        return localDate.isBefore(today);
    }

    /**
     * Gets the current date.
     *
     * @return The current date
     */
    public static Date getCurrentDate() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Gets the current date and time.
     *
     * @return The current date and time
     */
    public static Date getCurrentDateTime() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Formats a LocalDate to a string using the specified pattern.
     *
     * @param localDate The LocalDate to format
     * @param pattern The pattern to use for formatting
     * @return The formatted date string, or null if the localDate is null
     */
    public static String formatLocalDate(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    /**
     * Parses a date string to a LocalDate using the specified pattern.
     *
     * @param dateStr The date string to parse
     * @param pattern The pattern to use for parsing
     * @return The parsed LocalDate, or null if parsing fails
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing LocalDate: {0} with pattern: {1}", new Object[]{dateStr, pattern});
            return null;
        }
    }

    /**
     * Converts a Date to a LocalDate.
     *
     * @param date The Date to convert
     * @return The converted LocalDate, or null if the date is null
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Converts a LocalDate to a Date.
     *
     * @param localDate The LocalDate to convert
     * @return The converted Date, or null if the localDate is null
     */
    public static Date fromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}