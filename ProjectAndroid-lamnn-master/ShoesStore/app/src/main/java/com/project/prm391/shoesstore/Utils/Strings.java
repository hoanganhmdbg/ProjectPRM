package com.project.prm391.shoesstore.Utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by nguyen on 3/22/2018.
 */

public class Strings {
    private static final NumberFormat PRICE_FORMAT = new DecimalFormat("$ ###,###.##");
    private static final NumberFormat DISCOUNT_RATE_FORMAT = new DecimalFormat("-##%");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean containsIgnoreCase(String s, String keyword) {
        return Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(s).find();
    }

    public static String formatPrice(double price) {
        return PRICE_FORMAT.format(price);
    }

    public static String formatDiscountRate(double discountRate) {
        return DISCOUNT_RATE_FORMAT.format(discountRate);
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatTime(Date time) {
        return TIME_FORMAT.format(time);
    }
}
