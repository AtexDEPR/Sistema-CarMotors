/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for date operations
 */
public class DateUtil {
    private static final Logger LOGGER = Logger.getLogger(DateUtil.class.getName());

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Get current date as string in format yyyy-MM-dd
     *
     * @return Current date as string
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * Get current date and time as string in format yyyy-MM-dd HH:mm:ss
     *
     * @return Current date and time as string
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * Format date object to string in format yyyy-MM-dd
     *
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Format date object to string in format yyyy-MM-dd HH:mm:ss
     *
     * @param date Date to format
     * @return Formatted date and time string
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * Parse string to date object from format yyyy-MM-dd
     *
     * @param dateStr Date string to parse
     * @return Date object
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing date: {0}", dateStr);
            return null;
        }
    }

    /**
     * Parse string to date object from format yyyy-MM-dd HH:mm:ss
     *
     * @param dateTimeStr Date and time string to parse
     * @return Date object
     */
    public static Date parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
            return sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing date and time: {0}", dateTimeStr);
            return null;
        }
    }

    /**
     * Calculate days between two dates
     *
     * @param startDate Start date
     * @param endDate End date
     * @return Number of days between dates
     */
    public static long daysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    /**
     * Add days to a date
     *
     * @param date Base date
     * @param days Number of days to add
     * @return New date
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime() + (long) days * 24 * 60 * 60 * 1000);
    }
}