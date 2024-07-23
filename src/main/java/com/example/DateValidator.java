package com.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator {
    public static boolean isValid(final String date) {

        boolean valid;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.parse(date);
            valid = true;

        } catch (ParseException e) {
            valid = false;
        }

        return valid;
    }
}
