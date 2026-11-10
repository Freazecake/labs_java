package com.factory.util;

import java.text.SimpleDateFormat;

public class Validator {
    public static boolean isPositiveInt(String s) {
        try {
            return Integer.parseInt(s.trim()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String s) {
        try {
            return Double.parseDouble(s.trim()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isValidDate(String s) {
        if (!isNotBlank(s))
            return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            sdf.setLenient(false);
            sdf.parse(s.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}