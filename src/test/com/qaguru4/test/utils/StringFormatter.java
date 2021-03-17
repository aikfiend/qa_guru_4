package com.qaguru4.test.utils;

public class StringFormatter {
    public static String numberToOrdinal(String numStr) {
        int num = Integer.parseInt(numStr);

        if (num == 0) {
            throw new IllegalArgumentException(num + " is not a natural number");
        }

        if (num >= 11 && num <= 13) {
            return numStr.concat("th");
        }

        switch (num % 10) {
            case 1:
                return numStr.concat("st");
            case 2:
                return numStr.concat("nd");
            case 3:
                return numStr.concat("rd");
            default:
                return numStr.concat("th");
        }
    }
}