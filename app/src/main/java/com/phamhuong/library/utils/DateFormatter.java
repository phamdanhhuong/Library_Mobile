package com.phamhuong.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    
    public static String formatDate(String dateStr) {
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());
            
            Date date = apiFormat.parse(dateStr);
            if (date != null) {
                return displayFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}
