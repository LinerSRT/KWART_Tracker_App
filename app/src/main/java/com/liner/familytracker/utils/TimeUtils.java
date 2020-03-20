package com.liner.familytracker.utils;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TimeUtils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time, ImageView view) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            view.setColorFilter(Color.GREEN);
            return "Online";
        } else if (diff < 2 * MINUTE_MILLIS) {
            view.setColorFilter(Color.GREEN);
            return "Online";
        } else if (diff < 50 * MINUTE_MILLIS) {
            view.setColorFilter(Color.RED);
            return diff / MINUTE_MILLIS + " минут назад";
        } else if (diff < 90 * MINUTE_MILLIS) {
            view.setColorFilter(Color.RED);
            return "Час назад";
        } else if (diff < 24 * HOUR_MILLIS) {
            view.setColorFilter(Color.RED);
            return diff / HOUR_MILLIS + " часов назад";
        } else if (diff < 48 * HOUR_MILLIS) {
            view.setColorFilter(Color.RED);
            return "Вчера";
        } else {
            view.setColorFilter(Color.RED);
            return diff / DAY_MILLIS + " дней назад";
        }
    }
}
