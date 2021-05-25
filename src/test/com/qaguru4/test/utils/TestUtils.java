package com.qaguru4.test.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestUtils {
    public static String ordinalDayDateFormat(String dateStr) {
        return dateStr.replaceAll("(\\s[23]*1)(,\\s)", "$1st$2")
                .replaceAll("(\\s[2]*2)(,\\s)","$1nd$2")
                .replaceAll("(\\s[2]*3)(,\\s)", "$1rd$2")
                .replaceAll("([2]*[4-9]|[10-20]|30)(,\\s)", "$1th$2");
    }

    /**
     * @throws InvalidDateException when date is wrong or date format is not valid
     */
    public static boolean isValidDate(String dateStr) throws InvalidDateException {
        DateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException ex) {
            throw new InvalidDateException(
                    "Wrong date or date format is not valid, the date should be in format: MMM dd, YYYY",
                    ex
            );
        }
        return true;
    }

    public final static class InvalidDateException extends Exception {
        public InvalidDateException (final String msg, final Throwable cause) {
            super (msg, cause);
        }
    }
}